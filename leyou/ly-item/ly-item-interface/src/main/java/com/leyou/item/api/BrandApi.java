package com.leyou.item.api;

import com.leyou.item.pojo.Brand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author Yu, Zou
 * @version 1.0
 * @date 2022/2/14 13:59
 */
@RequestMapping("/brand")
public interface BrandApi {

    /**
     * 查询品牌接口
     * @param ids
     * @return
     */
    @GetMapping("/list")
    ResponseEntity<List<Brand>> queryBrandByIds(@RequestParam("ids") List<Long> ids);
}
