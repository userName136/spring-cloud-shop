package com.leyou.search.client;

import com.leyou.item.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Yu, Zou
 * @version 1.0
 * @date 2022/2/9 15:25
 */
@FeignClient(value = "item-service")
public interface CategoryClient extends CategoryApi {
}
