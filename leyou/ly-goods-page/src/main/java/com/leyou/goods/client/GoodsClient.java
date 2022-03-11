package com.leyou.goods.client;

import com.leyou.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Yu, Zou
 * @version 1.0
 * @date 2022/2/17 14:55
 */
@FeignClient("item-service")
public interface GoodsClient extends GoodsApi {
}
