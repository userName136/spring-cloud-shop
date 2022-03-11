package com.leyou.item.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author Yu, Zou
 * @version 1.0
 * @date 2022/2/9 10:32
 */
@RequestMapping("/category")
public interface CategoryApi {

    @GetMapping("/names")
    ResponseEntity<List<String>> queryNameByIds(@RequestParam("ids") List<Long> ids);

}
