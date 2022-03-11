package com.leyou.item.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.leyou.item.mapper.FileInfoMapper;
import com.leyou.item.pojo.FileInfo;
import com.leyou.item.pojo.PageResult;
import com.leyou.item.service.FileInfoService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

/**
 * @author Yu, Zou
 * @version 1.0
 * @date 2022/1/14 17:07
 */
@Service
public class FileInfoServiceImpl implements FileInfoService {

    @Autowired
    private FileInfoMapper fMapper;

    @Override
    public PageResult<FileInfo> queryFileInfo(Integer page,Integer rows, String sortBy, Boolean desc) {
        // 开始分页
        PageHelper.startPage(page, rows);
        // 过滤
        Example example = new Example(FileInfo.class);
        if (StringUtils.isNotBlank(sortBy)) {
            // 排序
            String orderByClause = sortBy + (desc ? " DESC" : " ASC");
            example.setOrderByClause(orderByClause);
        }
        // 查询
        Page<FileInfo> pageInfo = (Page<FileInfo>) fMapper.selectByExample(example);
        // 返回结果
        return new PageResult<>(pageInfo.getTotal(), pageInfo);
    }
}
