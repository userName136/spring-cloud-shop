package com.leyou.goods.service;

import com.leyou.item.pojo.Category;
import com.leyou.item.pojo.Spu;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author Yu, Zou
 * @version 1.0
 * @date 2022/2/17 14:58
 */
@Component
public interface GoodsPageService {

    /**
     * 封装数据模型
     * @param id
     * @return
     */
    Map<String, Object> loadModel(Long id);

}
