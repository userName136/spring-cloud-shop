package com.leyou.item.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Yu, Zou
 * @version 1.0
 * @date 2022/2/15 10:12
 */
@RequestMapping("/spec")
public interface SpecificationApi {

    /**
     * 根据id查询规格信息
     * @param id
     * @return
     */
    @GetMapping("/query/{id}")
    ResponseEntity<String> querySpecificationByCategoryId(@PathVariable("id") Long id);

}
