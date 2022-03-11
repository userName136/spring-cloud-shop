package com.leyou.goods.client;

import com.leyou.item.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Yu, Zou
 * @version 1.0
 * @date 2022/2/17 14:56
 */
@FeignClient("item-service")
public interface BrandClient extends BrandApi {
}
