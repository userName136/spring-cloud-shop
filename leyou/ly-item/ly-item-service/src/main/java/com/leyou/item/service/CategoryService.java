package com.leyou.item.service;

import com.leyou.item.pojo.Category;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Yu, Zou
 * @version 1.0
 * @date 2022/1/5 13:52
 */
@Component
public interface CategoryService {

    /**
     * 根据id查询
      * @param id
     * @return
     */
   List<Category> queryByParentId(Long id);

    /**
     * 通过品牌id查询商品分类
     * @param bid
     * @return
     */
    List<Category> queryByBrandId(Long bid);

    /**
     * 查询名称
     * @param ids
     * @return
     */
    List<String> queryNameByIds(List<Long> ids);

    /**
     * 根据3级分类id，查询1~3级的分类
     * @param id
     * @return
     */
    List<Category> queryAllByCid3(Long id);
}
