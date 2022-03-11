package com.leyou.item.service;

import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.PageResult;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Yu, Zou
 * @version 1.0
 * @date 2022/1/5 15:42
 */
@Component
public interface BrandService {

    /**
     * 品牌查询
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @param key
     * @return
     */
    PageResult<Brand> queryBrandByPageAndSort(Integer page, Integer rows, String sortBy, Boolean desc, String key);

    /**
     * 新增品牌
     * @param brand
     * @param cIds
     */
    void saveBrand(Brand brand, List<Long> cIds);

    /**
     * 编辑品牌
     * @param brand
     * @param cIds
     */
    void editBrand(Brand brand, List<Long> cIds);

    /**
     * 删除品牌
     * @param bId
     */
    void deleteBrand(Long bId);

    /**
     * 根据分类查询品牌
     * @param cid
     * @return
     */
    List<Brand> queryBrandByCategory(Long cid);

    /**
     * 根据多个id查询品牌
     * @param ids
     * @return
     */
    List<Brand> queryBrandByIds(List<Long> ids);
}
