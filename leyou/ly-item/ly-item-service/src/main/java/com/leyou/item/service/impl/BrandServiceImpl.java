package com.leyou.item.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.PageResult;
import com.leyou.item.service.BrandService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.naming.ldap.PagedResultsControl;
import java.util.List;

/**
 * @author Yu, Zou
 * @version 1.0
 * @date 2022/1/5 15:51
 */
@Service("bService")
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandMapper bMapper;

    @Override
    public PageResult<Brand> queryBrandByPageAndSort(Integer page, Integer rows, String sortBy, Boolean desc, String key) {
        // 开始分页
        PageHelper.startPage(page, rows);
        // 过滤
        Example example = new Example(Brand.class);
        if (StringUtils.isNotBlank(key)) {
            example.createCriteria().andLike("name", "%" + key + "%")
                    .orEqualTo("letter", key);
        }
        if (StringUtils.isNotBlank(sortBy)) {
            // 排序
            String orderByClause = sortBy + (desc ? " DESC" : " ASC");
            example.setOrderByClause(orderByClause);
        }
        // 查询
        Page<Brand> pageInfo = (Page<Brand>) bMapper.selectByExample(example);
        // 返回结果
        return new PageResult<>(pageInfo.getTotal(), pageInfo);
    }

    @Override
    public void saveBrand(Brand brand, List<Long> cIds) {
        // 新增品牌信息
        this.bMapper.insertSelective(brand);
        // 新增品牌和分类中间表
        for (Long cId : cIds) {
            this.bMapper.insertCategoryBrand(cId, brand.getId());
        }
    }

    @Override
    public void editBrand(Brand brand, List<Long> cIds) {
        // 修改品牌信息
        this.bMapper.updateByPrimaryKeySelective(brand);
        // 修改品牌和分类中间表
        //修改前，先将该商品的分类信息全部删除，再重新添加
        this.bMapper.deleteCategoryBrand(brand.getId());
        for (Long cId : cIds) {
            this.bMapper.insertCategoryBrand(cId, brand.getId());
        }
    }

    @Override
    public void deleteBrand(Long bId) {
        //删除品牌信息
        this.bMapper.deleteBrandById(bId);
        //维护中间表，删除相关数据
        this.bMapper.deleteCategoryBrand(bId);
    }

    @Override
    public List<Brand> queryBrandByCategory(Long cid) {
        return this.bMapper.queryByCategoryId(cid);
    }

    @Override
    public List<Brand> queryBrandByIds(List<Long> ids) {
        return this.bMapper.selectByIdList(ids);
    }
}
