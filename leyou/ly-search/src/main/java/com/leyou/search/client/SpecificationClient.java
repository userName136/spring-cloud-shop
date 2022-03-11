package com.leyou.search.client;

import com.leyou.item.api.SpecificationApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Yu, Zou
 * @version 1.0
 * @date 2022/2/15 10:11
 */
@FeignClient("item-service")
public interface SpecificationClient extends SpecificationApi {
}
