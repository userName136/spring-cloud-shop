package com.leyou.search.pojo;

import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Category;
import com.leyou.item.pojo.PageResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author Yu, Zou
 * @version 1.0
 * @date 2022/2/14 13:54
 */
public class SearchReturn extends PageResult<Goods> {

    private List<Category> categories;

    private List<Brand> brands;

    private List<Map<String, Object>> specs;

    public SearchReturn(Long total, Long totalPage,
                        List<Goods> items, List<Category> categories,
                        List<Brand> brands,
                        List<Map<String, Object>> specs
                                    ) {
        super(total, totalPage, items);
        this.categories = categories;
        this.brands = brands;
        this.specs = specs;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Brand> getBrands() {
        return brands;
    }

    public void setBrands(List<Brand> brands) {
        this.brands = brands;
    }

    public List<Map<String, Object>> getSpecs() {
        return specs;
    }

    public void setSpecs(List<Map<String, Object>> specs) {
        this.specs = specs;
    }

}
