package com.leyou.item.service.impl;

import com.leyou.item.pojo.Category;
import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Yu, Zou
 * @version 1.0
 * @date 2022/1/5 13:53
 */
@Service("cService")
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper cMapper;

    @Override
    public List<Category> queryByParentId(Long pId) {
        Category category = new Category();
        category.setParentId(pId);
        return cMapper.select(category);
    }

    @Override
    public List<Category> queryByBrandId(Long bid) {
        return this.cMapper.queryByBrandId(bid);
    }

    @Override
    public List<String> queryNameByIds(List<Long> ids) {
        return this.cMapper.selectByIdList(ids).stream().map(Category::getName).collect(Collectors.toList());
    }

    @Override
    public List<Category> queryAllByCid3(Long id) {
        Category c3 = this.cMapper.selectByPrimaryKey(id);
        Category c2 = this.cMapper.selectByPrimaryKey(c3.getParentId());
        Category c1 = this.cMapper.selectByPrimaryKey(c2.getParentId());
        return Arrays.asList(c1,c2,c3);
    }
}
