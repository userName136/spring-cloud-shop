package com.leyou.item.mapper;

import com.leyou.item.pojo.Sku;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

/**
 * @author Yu, Zou
 * @version 1.0
 * @date 2022/1/13 11:42
 */
@Component
public interface SkuMapper extends Mapper<Sku>, InsertListMapper<Sku> {
}
