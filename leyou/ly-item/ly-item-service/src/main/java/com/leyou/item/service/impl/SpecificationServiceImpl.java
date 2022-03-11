package com.leyou.item.service.impl;

import com.leyou.item.mapper.SpecificationMapper;
import com.leyou.item.pojo.Specification;
import com.leyou.item.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Yu, Zou
 * @version 1.0
 * @date 2022/1/12 16:28
 */
@Service
public class SpecificationServiceImpl implements SpecificationService {

    @Autowired
    private SpecificationMapper sMapper;

    @Override
    public Specification queryById(Long id) {
        return this.sMapper.selectByPrimaryKey(id);
    }

    @Override
    public Long queryCIdBySpecId(Long id) {
        return this.sMapper.queryCIdBySpecId(id);
    }

    @Override
    public void insertSpecification(Long cId, String specifications) {
        this.sMapper.insertSpecification(cId,specifications);
    }

    @Override
    public void updateSpecification(Long cId, String specifications) {
        this.sMapper.updateSpecification(cId,specifications);
    }
}
