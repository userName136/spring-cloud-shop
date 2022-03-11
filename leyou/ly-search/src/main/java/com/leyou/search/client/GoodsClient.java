package com.leyou.search.client;

import com.leyou.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Yu, Zou
 * @version 1.0
 * @date 2022/2/9 15:26
 */
@FeignClient(value = "item-service")
public interface GoodsClient extends GoodsApi {
}
