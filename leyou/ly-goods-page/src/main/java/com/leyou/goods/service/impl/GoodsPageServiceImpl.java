package com.leyou.goods.service.impl;

import com.leyou.goods.client.BrandClient;
import com.leyou.goods.client.CategoryClient;
import com.leyou.goods.client.GoodsClient;
import com.leyou.goods.service.GoodsPageService;
import com.leyou.item.pojo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Yu, Zou
 * @version 1.0
 * @date 2022/2/17 14:58
 */
@Service
public class GoodsPageServiceImpl implements GoodsPageService {

        @Autowired
        private GoodsClient goodsClient;

        @Autowired
        private CategoryClient categoryClient;

        @Autowired
        private BrandClient brandClient;

        private static final Logger logger = LoggerFactory.getLogger(GoodsPageServiceImpl.class);

        @Override
        public Map<String, Object> loadModel(Long id) {
            try {
                // 模型数据
                Map<String, Object> modelMap = new HashMap<>();

                // 1、查询spu
                ResponseEntity<Spu> spuResp = this.goodsClient.querySpuById(id);
                // 2、查询spuDetail
                ResponseEntity<SpuDetail> detailResp = this.goodsClient.querySpuDetailById(id);
                // 3、查询sku
                ResponseEntity<List<Sku>> skusResp = this.goodsClient.querySkuBySpuId(id);
                if (!spuResp.hasBody() || !detailResp.hasBody() || !skusResp.hasBody()) {
                    return null;
                }
                Spu spu = spuResp.getBody();
                SpuDetail detail = detailResp.getBody();
                List<Sku> skus = skusResp.getBody();

                // 装填模型数据
                modelMap.put("spu", spu);
                modelMap.put("spuDetail", detail);
                modelMap.put("skus", skus);

                // 4、准备商品分类
                List<Category> categories = getCategories(spu);
                if (categories != null) {
                    modelMap.put("categories", categories);
                }

                // 5、准备品牌数据
                ResponseEntity<List<Brand>> brandResp = this.brandClient.queryBrandByIds(
                        Arrays.asList(spu.getBrandId()));
                if (brandResp.hasBody()) {
                    modelMap.put("brand", brandResp.getBody().get(0));
                }
                return modelMap;

            } catch (Exception e) {
                logger.error("加载商品数据出错,spuId:{}", id, e);
            }
            return null;
        }

        private List<Category> getCategories(Spu spu) {
            try {
                ResponseEntity<List<String>> categoryResp = this.categoryClient.queryNameByIds(
                        Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
                if (categoryResp.hasBody()) {
                    List<String> names = categoryResp.getBody();
                    Category c1 = new Category();
                    c1.setName(names.get(0));
                    c1.setId(spu.getCid1());
                    Category c2 = new Category();
                    c2.setName(names.get(1));
                    c2.setId(spu.getCid2());
                    Category c3 = new Category();
                    c3.setName(names.get(2));
                    c3.setId(spu.getCid3());
                    return Arrays.asList(c1, c2, c3);
                }

            } catch (Exception e) {
                logger.error("查询商品分类出错，spuId：{}", spu.getId(), e);
            }
            return null;
        }
}
