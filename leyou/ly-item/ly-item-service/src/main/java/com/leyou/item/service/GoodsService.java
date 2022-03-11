package com.leyou.item.service;

import com.leyou.item.pojo.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Yu, Zou
 * @version 1.0
 * @date 2022/1/13 10:05
 */
@Component
public interface GoodsService {

    /**
     * 查询商品列表
     * @param page
     * @param rows
     * @param saleable
     * @param key
     * @param desc
     * @param sortBy
     * @return
     */
    PageResult<SpuBo> querySpuByPageAndSort(Integer page, Integer rows, Boolean saleable, String key,Boolean desc,String sortBy);

    /**
     * 保存商品信息
     * @param spu
     */
    void save(SpuBo spu);

    /**
     * 查询spuDetail数据。
     * @param id
     * @return
     */
    SpuDetail querySpuDetailById(Long id);

    /**
     * 查询sku数据
     * @param id
     * @return
     */
    List<Sku> querySkuBySpuId(Long id);

    /**
     * 修改商品
     * @param spu
     */
    void updateGoods(SpuBo spu);

    /**
     * 删除商品
     * @param id
     */
    void deleteGoods(Long id);

    /**
     * 修改商品上/下架状态
     * @param id
     * @param option
     */
    void changeSaleableById(Long id, Integer option);

    /**
     * 根据spu的id查询spu
     * @param id
     * @return
     */
    Spu querySpuById(Long id);
}
