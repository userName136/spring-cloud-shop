package com.leyou.search.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Category;
import com.leyou.item.pojo.NumberUtils;
import com.leyou.item.pojo.PageResult;
import com.leyou.search.client.BrandClient;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.client.SpecificationClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchResult;
import com.leyou.search.pojo.SearchReturn;
import com.leyou.search.service.GoodsService;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchService;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.ParsedHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.Stats;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * @author Yu, Zou
 * @version 1.0
 * @date 2022/2/9 15:31
 */
@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private SpecificationClient specificationClient;

    @Autowired
    private CategoryClient categoryClient;

    private static final Logger logger = LoggerFactory.getLogger(SearchService.class);

    private static ObjectMapper mapper=new ObjectMapper();

    @Override
    public PageResult<Goods> search(SearchResult request) throws IOException {
        String key = request.getKey();
        // 判断是否有搜索条件，如果没有，直接返回null。不允许搜索全部商品
        if (StringUtils.isBlank(key)) {
            return null;
        }
        // 准备分页参数
        int page = (request.getPage()-1)*20;
        int size = request.getSize();

        // 构建查询条件
        SearchRequest sRequest = new SearchRequest("ly_goods");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        // 1、通过sourceFilter设置返回的结果字段,我们只需要id、skus、subTitle
        String[] includes = new String[]{"id","skus","subTitle"};
        sourceBuilder.fetchSource(includes,null);
        // 2、对key进行全文检索查询
        QueryBuilder queryBuilder = buildBasicQueryWithFilter(request);
        sourceBuilder.query(queryBuilder);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        // 3、分页
        sourceBuilder.from(page);
        sourceBuilder.size(size);
        //4.排序
        String sortBy = request.getSortBy();
        Boolean desc = request.getDescending();
        if(StringUtils.isNotBlank(sortBy)){
            sourceBuilder.sort(SortBuilders.fieldSort(sortBy).order(desc ? SortOrder.DESC : SortOrder.ASC));
        }
        //5.聚合
        // 商品分类聚合名称
        String categoryAggName = "category";
        // 品牌聚合名称
        String brandAggName = "brand";
        // 对商品分类进行聚合
        sourceBuilder.aggregation(AggregationBuilders.terms(categoryAggName).field("cid3"));
        // 对品牌进行聚合
        sourceBuilder.aggregation(AggregationBuilders.terms(brandAggName).field("brandId"));
        //关键字高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("skus")
                .requireFieldMatch(false)
                .fragmentSize(100000)
                .preTags("<span style='color:red'>")
                .postTags("</span>");
        sourceBuilder.highlighter(highlightBuilder);
        // 查询，获取结果
        sRequest.source(sourceBuilder);
        SearchResponse response = client.search(sRequest, RequestOptions.DEFAULT);
        // 5.1、商品分类的聚合结果
        Map<String, Aggregation> aggMap = response.getAggregations().asMap();
        List<Category> categories = getCategoryAggResult(aggMap, categoryAggName);
        List<Map<String, Object>> specs = null;
        if(categories.size() == 1){
            specs = getSpecs(categories.get(0).getId(),queryBuilder);
        }
        // 5.2、品牌的聚合结果
        List<Brand> brands = getBrandAggResult(aggMap, brandAggName);
        List<Goods> results = new ArrayList<>();
        for (SearchHit hit : response.getHits().getHits()) {
            //高亮
            HighlightField title = hit.getHighlightFields().get("skus");
            //原来的结果
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            Goods g = new Goods();
            String id = String.valueOf(sourceAsMap.get("id"));
            g.setId(Long.valueOf(id));
            g.setSubTitle((String) sourceAsMap.get("subTitle"));
            //解析高亮的字段，将原来的字段换为高亮的字段
            if(title!=null){
                Text[] fragments = title.fragments();
                String newTitle = "";
                for (Text text : fragments) {
                    newTitle += text;
                }
                sourceAsMap.put("skus",newTitle);
            }
            g.setSkus((String) sourceAsMap.get("skus"));
            results.add(g);
        }
        // 封装结果并返回
        //总条数
        String[] str = String.valueOf(response.getHits().getTotalHits()).split(" ");
        Long total = Long.valueOf(str[0]);
        //总页码数
        Long totalPage = Long.valueOf((total.intValue() + size - 1) / size);
        return new SearchReturn(total,totalPage, results,categories,brands,specs);
    }
//方法调用顺序-->search()-->getSpecs()-->getNumericalInterval()-->getInterval()-->aggForSpec()
    /**
     * 聚合规格参数
     * @param id
     * @param query
     * @return
     */
    private List<Map<String, Object>> getSpecs(Long id, QueryBuilder query) throws IOException {
        //根据分类查询规格
        ResponseEntity<String> specResp = this.specificationClient.querySpecificationByCategoryId(id);
        if(!specResp.hasBody()){
            logger.error("查询规格参数出错，id为{}",id);
        }
        //将结果反序列化
        String jsonSpec = specResp.getBody();
        List<Map<String, Object>> specs = null;
        try{
            specs = mapper.readValue(jsonSpec, new TypeReference<List<Map<String, Object>>>(){});
        }catch (IOException e){
            logger.error("解析规格的JSON数据失败,JSON是{}",jsonSpec,e);
            return null;
        }
        //准备集合，保存规格字符串参数名
        Set<String> strSpec = new HashSet<>();
        //准备map，保存数值规格参数名及单位
        Map<String, String> numericalUnits = new HashMap<>();
        //解析规格
        for (Map<String, Object> spec : specs) {
            for (Map<String, Object> params : (List<Map<String, Object>>) spec.get("params")) {
                Boolean searchable = (Boolean) params.get("searchable");
                if(searchable){
                    //判断是否是数值类型
                    if(params.containsKey("numerical") && (boolean)params.get("numerical")){
                        numericalUnits.put(params.get("k").toString(), params.get("unit").toString());
                    }else{
                        strSpec.add(params.get("k").toString());
                    }
                }
            }
        }
        // 聚合计算数值类型规格的interval
        Map<String, Double> numericalInterval = getNumericalInterval(id, numericalUnits.keySet());
        return aggForSpec(strSpec,numericalInterval,numericalUnits,query);
    }

    /**
     * 根据规格参数，聚合得出过滤条件
     * @param strSpec
     * @param numericalInterval
     * @param numericalUnits
     * @param query
     * @return
     */
    private List<Map<String, Object>> aggForSpec(Set<String> strSpec,
                                                 Map<String, Double> numericalInterval,
                                                 Map<String, String> numericalUnits,
                                                 QueryBuilder query) throws IOException {
        List<Map<String, Object>> specs = new ArrayList<>();
        // 准备查询条件
        SearchRequest sRequest = new SearchRequest();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(query);
        // 聚合数值类型
        numericalInterval.entrySet().forEach(entry ->
                sourceBuilder.aggregation(
                        AggregationBuilders.histogram(entry.getKey())
                                .field("specs." + entry.getKey())
                                .interval(entry.getValue())
                                .minDocCount(1)
                )
        );
        // 聚合字符串
        strSpec.forEach(key ->
                sourceBuilder.aggregation(AggregationBuilders.terms(key).field("specs." + key + ".keyword"))
        );
        //查询结果
        sRequest.source(sourceBuilder);
        SearchResponse response = this.client.search(sRequest, RequestOptions.DEFAULT);
        // 解析聚合结果
        Map<String,Aggregation> aggregations = response.getAggregations().asMap();
        // 解析数值类型
        numericalInterval.entrySet().forEach(entry -> {
            Map<String,Object> spec = new HashMap<>();
            String key = entry.getKey();
            spec.put("k", key);
            spec.put("unit", numericalUnits.get(key));
            ParsedHistogram histogram = (ParsedHistogram) aggregations.get(key);
            spec.put("options", histogram.getBuckets().stream().map(bucket -> {
                Double begin = (double) bucket.getKey();
                Double end = begin + numericalInterval.get(key);
                // 对begin和end取整
                if (NumberUtils.isInt(begin) && NumberUtils.isInt(end)) {
                    // 确实是整数，需要取整
                    return begin.intValue() + "-" + end.intValue();
                } else {
                    // 小数，取2位小数
                    begin = NumberUtils.scale(begin, 2);
                    end = NumberUtils.scale(end, 2);
                    return begin + "-" + end;
                }
            }));
            specs.add(spec);
        });
        // 解析字符串类型
        strSpec.forEach(key -> {
            Map<String,Object> spec = new HashMap<>();
            spec.put("k", key);
            ParsedStringTerms terms = (ParsedStringTerms) aggregations.get(key);
            spec.put("options", terms.getBuckets().stream().map(bucket -> bucket.getKeyAsString()));
            specs.add(spec);
        });
        return specs;
    }

    /**
     * 聚合得到interval
     * @param cid
     * @param keySet
     * @return
     */
    private Map<String,Double> getNumericalInterval(Long cid, Set<String> keySet) throws IOException {
        Map<String,Double> numbericalSpecs = new HashMap<>();
            SearchRequest sRequest = new SearchRequest();
            //准备查询条件
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            //不查询任何数据
            sourceBuilder.query(QueryBuilders.termQuery("cid3",cid.toString()))
                    .fetchSource(new String[]{},null);
            // 添加stats类型的聚合
            keySet.forEach(key ->{
                sourceBuilder.aggregation(AggregationBuilders.stats(key) .field("specs." + key));
            });
            //查询结果
            sRequest.source(sourceBuilder);
            SearchResponse response = client.search(sRequest, RequestOptions.DEFAULT);
            //解析聚合结果
            keySet.forEach(key -> {
                Stats stats = response.getAggregations().get(key);
                numbericalSpecs.put(key,getInterval(stats.getMin(), stats.getMax(), stats.getSum()));
            });
            return numbericalSpecs;
        }

    /**
     * 根据最小值，最大值，sum计算interval
     * @param min
     * @param max
     * @param sum
     * @return
     */
    private double getInterval(double min,double max,Double sum){
            double interval = (max - min)/ 6.0d ;
            //判断是否是小数
            if (sum.intValue() == sum) {
                //不是小数，要取整十、整百这样
                //根据interval的整数位长度来判断位数
                int length = StringUtils.substringBefore(String.valueOf(interval),"." ).length();
                double factor = Math.pow(10.0,length - 1);
                return Math.round(interval / factor) * factor;
            }else {
                //是小数,我们只保留一位小数
                return NumberUtils.scale(interval,1);
            }
    }

/**
 * 解析商品分类聚合结果
 * @param aggMap
 * @param categoryAggName
 * @return
 */
    private List<Category> getCategoryAggResult(Map<String, Aggregation> aggMap,String categoryAggName){
        List<Category> categories = new ArrayList<>();
        Terms category= (Terms) aggMap.get(categoryAggName);
        List<Long> cIds = new ArrayList<>();
        for (Terms.Bucket bucket : category.getBuckets()) {
            cIds.add(bucket.getKeyAsNumber().longValue());
        }
        // 根据id查询分类名称
        ResponseEntity<List<String>> categoryResp = this.categoryClient.queryNameByIds(cIds);
        if (!categoryResp.hasBody()) {
            logger.error("查询分类出现错误，id为{}", cIds);
            return null;
        }
        for (int i = 0; i < categoryResp.getBody().size(); i++) {
            Category c = new Category();
            c.setId(cIds.get(i));
            c.setName(categoryResp.getBody().get(i));
            categories.add(c);
        }
        return categories;
    }

    /**
     * 解析品牌聚合结果
     * @param aggMap
     * @param brandAggName
     * @return
     */
    private List<Brand> getBrandAggResult(Map<String, Aggregation> aggMap, String brandAggName) {
        Terms brand= (Terms) aggMap.get(brandAggName);
        List<Long> bIds = new ArrayList<>();
        for (Terms.Bucket bucket : brand.getBuckets()) {
            bIds.add(bucket.getKeyAsNumber().longValue());
        }
        // 根据id查询品牌
        ResponseEntity<List<Brand>> brandResp = this.brandClient.queryBrandByIds(bIds);
        if (!brandResp.hasBody()) {
            logger.error("查询品牌出现错误，id为{}", bIds);
            return null;
        }
        return brandResp.getBody();
    }

    /**
     * 构建基本查询条件
     * @param request
     * @return
     */
    private QueryBuilder buildBasicQueryWithFilter(SearchResult request) {
        System.out.println("request--->>"+request);
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        // 基本查询条件
        queryBuilder.must(QueryBuilders.matchQuery("all", request.getKey()).operator(Operator.AND));
        // 过滤条件构建器
        BoolQueryBuilder filterQueryBuilder = QueryBuilders.boolQuery();
        // 整理过滤条件
        Map<String, String> filter = request.getFilter();
        for (Map.Entry<String, String> entry : filter.entrySet()) {
            // 判断是否是数值类型
            String key = entry.getKey();
            // 判断是否是数值类型
            String value = entry.getValue();
            String regex = "^(\\d+\\.?\\d*)-(\\d+\\.?\\d*)$";

            if (value.matches(regex)) {
                Double[] nums = NumberUtils.searchNumber(value, regex);
                // 数值类型进行范围查询
                filterQueryBuilder.must(QueryBuilders.rangeQuery("specs." + key).gte(nums[0]).lt(nums[1]));
            } else {
                if (key != "cid3" && key != "brandId") {
                    key = "specs." + key + ".keyword";
                }
                // 字符串类型，进行term查询
                filterQueryBuilder.must(QueryBuilders.termQuery(key, value));
            }
        }
        // 添加过滤条件
        queryBuilder.filter(filterQueryBuilder);
        return queryBuilder;
    }

}
