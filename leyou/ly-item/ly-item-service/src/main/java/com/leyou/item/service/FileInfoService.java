package com.leyou.item.service;

import com.leyou.item.pojo.FileInfo;
import com.leyou.item.pojo.PageResult;

/**
 * @author Yu, Zou
 * @version 1.0
 * @date 2022/1/14 17:06
 */
public interface FileInfoService {

    /**
     * 查询上传文件信息
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @return
     */
    PageResult<FileInfo> queryFileInfo(Integer page, Integer rows, String sortBy, Boolean desc);

}
