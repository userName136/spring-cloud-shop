package com.leyou.goods;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author Yu, Zou
 * @version 1.0
 * @date 2022/2/17 14:08
 */
@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class LyGoodsPageApplication {

    public static void main(String[] args) {
        SpringApplication.run(LyGoodsPageApplication.class,args);
    }

}
