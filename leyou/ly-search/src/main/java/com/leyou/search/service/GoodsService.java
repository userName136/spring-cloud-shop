package com.leyou.search.service;

import com.leyou.item.pojo.PageResult;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchResult;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Yu, Zou
 * @version 1.0
 * @date 2022/2/9 15:31
 */
@Component
public interface GoodsService {

    /**
     * 关键字搜索
     * @param request
     * @return
     */
  PageResult<Goods> search(SearchResult request) throws IOException;

}
