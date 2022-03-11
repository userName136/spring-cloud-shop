package com.leyou.search.client;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leyou.item.pojo.PageResult;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.SpuBo;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.search.LySearchApplication;

import com.leyou.search.pojo.Goods;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.xcontent.XContentType;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.*;

/**
 * @author Yu, Zou
 * @version 1.0
 * @date 2022/2/9 15:32
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LySearchApplication.class)
public class CategoryClientTest {

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private RestHighLevelClient client;


//    @Test
//    public void testQueryCategories() {
//        ResponseEntity<List<String>> resp = this.categoryClient.queryNameByIds(Arrays.asList(1L, 2L, 3L));
//        resp.getBody().forEach(System.out::println);
//    }

    @Test
    public void loadData() throws IOException {
        int page = 1;
        int rows = 100;
        int size = 0;
        // 创建Goods集合
        List<Goods> goodsList = new ArrayList<>();
        do {
            ResponseEntity<PageResult<SpuBo>> resp = this.goodsClient.querySpuByPage(page, rows, true, null);
            // 判断状态
            if (!resp.hasBody()) {
                // 说明没查找到
                break;
            }
            // 有数据
            PageResult<SpuBo> result = resp.getBody();
            List<SpuBo> spus = result.getItems();
            size = spus.size();

            // 遍历spu
            for (SpuBo spu : spus) {
                // 查询sku信息
                ResponseEntity<List<Sku>> skuResp = this.goodsClient.querySkuBySpuId(spu.getId());
                // 查询详情
                ResponseEntity<SpuDetail> detailResp = this.goodsClient.querySpuDetailById(spu.getId());
                ResponseEntity<List<String>> categoryResp = this.categoryClient.queryNameByIds(
                        Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
                if (!skuResp.hasBody() || !detailResp.hasBody() || !categoryResp.hasBody()) {
                    return;
                }
                List<Sku> skus = skuResp.getBody();
                SpuDetail detail = detailResp.getBody();
                List<String> categoryNames = categoryResp.getBody();
                ObjectMapper mapper = new ObjectMapper();


                // 准备sku集合
                List<Map<String, Object>> skuList = new ArrayList<>();
                // 准备价格集合
                Set<Long> price = new HashSet<>();
                for (Sku s : skus) {
                    price.add(s.getPrice());
                    Map<String, Object> sku = new HashMap<>();
                    sku.put("id", s.getId());
                    sku.put("price", s.getPrice());
                    sku.put("image", StringUtils.isBlank(s.getImages()) ? "" : s.getImages().split(",")[0]);
                    sku.put("title", s.getTitle());
                    skuList.add(sku);
                }

                // 获取商品详情中的规格模板
                List<Map<String, Object>> specTemplate = mapper.readValue(detail.getSpecifications(), new TypeReference<List<Map<String, Object>>>() {
                });
                Map<String, Object> specs = new HashMap<>();
                // 过滤规格模板，把所有可搜索的信息保存到Map中
                specTemplate.forEach(m -> {
                    List<Map<String, Object>> params = (List<Map<String, Object>>) m.get("params");
                    params.forEach(p -> {
                        if ((boolean) p.get("searchable")) {
                            if (p.get("v") != null) {
                                specs.put(p.get("k").toString(), p.get("v"));
                            } else if (p.get("options") != null) {
                                specs.put(p.get("k").toString(), p.get("options"));
                            }
                        }
                    });
                });

                Goods goods = new Goods();
                goods.setBrandId(spu.getBrandId());
                goods.setCid1(spu.getCid1());
                goods.setCid2(spu.getCid2());
                goods.setCid3(spu.getCid3());
                goods.setCreateTime(spu.getCreateTime());
                goods.setId(spu.getId());
                goods.setSubTitle(spu.getSubTitle());
                goods.setAll(spu.getTitle() + " " + StringUtils.join(categoryNames, " ")); //全文检索字段
                goods.setPrice(new ArrayList<>(price));
                goods.setSkus(mapper.writeValueAsString(skuList));
                goods.setSpecs(specs);// TODO 用于搜索的规格参数集合

                goodsList.add(goods);
            }
            page++;
        } while (size == 100);
        //批量插入数据
        GetIndexRequest getResult = new GetIndexRequest("ly_goods");
        //客户端执行请求
        boolean getResponse = client.indices().exists(getResult, RequestOptions.DEFAULT);
        if(getResponse){
            BulkRequest request = new BulkRequest();
            request.timeout("5m");
            for (Goods g : goodsList) {
            request.add(new IndexRequest("ly_goods").source(JSON.toJSONString(g), XContentType.JSON));
            }
            BulkResponse response = client.bulk(request, RequestOptions.DEFAULT);
            System.out.println("插入数据是否失败-->>"+response.hasFailures());
        }else{
            System.out.println("该索引不存在!!!");
        }
    }

}