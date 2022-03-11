package com.leyou.item.service;

import com.leyou.item.pojo.Specification;

/**
 * @author Yu, Zou
 * @version 1.0
 * @date 2022/1/12 16:28
 */
public interface SpecificationService {

    /**
     * 查询商品列表信息
     * @param id
     * @return
     */
    Specification queryById(Long id);

    /**
     * 根据id查询该列表信息是否存在
     * @param id
     * @return
     */
    Long queryCIdBySpecId(Long id);

    /**
     * 添加商品列表信息
     * @param cId
     * @param specifications
     */
    void insertSpecification(Long cId, String specifications);

    /**
     * 修改商品列表信息
     * @param cId
     * @param specifications
     */
    void updateSpecification(Long cId, String specifications);
}
