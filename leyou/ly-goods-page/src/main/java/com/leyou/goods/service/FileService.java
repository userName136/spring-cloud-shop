package com.leyou.goods.service;

import org.springframework.stereotype.Component;

/**
 * @author Yu, Zou
 * @version 1.0
 * @date 2022/2/17 16:34
 */
@Component
public interface FileService {

    /**
     * 创建html页面
     * @param id
     * @throws Exception
     */
    void createHtml(Long id) throws Exception;

    /**
     * 判断某个商品的页面是否存在
     * @param id
     * @return
     */
    boolean exists(Long id);

    /**
     * 异步创建html页面
     * @param id
     */
    void syncCreateHtml(Long id);
}
