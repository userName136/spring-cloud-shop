package com.leyou.search.controller;

import com.leyou.item.pojo.PageResult;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchResult;
import com.leyou.search.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author Yu, Zou
 * @version 1.0
 * @date 2022/2/10 15:57
 */
@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private GoodsService goodsService;

    /**
     * 搜索商品
     *
     * @param searchResult
     * @return
     */
    @PostMapping("/page")
    public ResponseEntity<PageResult<Goods>> search(@RequestBody SearchResult searchResult) throws IOException {
        PageResult<Goods> result = this.goodsService.search(searchResult);
        if (result == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(result);
    }
}
