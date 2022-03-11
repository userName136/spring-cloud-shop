package com.leyou.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Yu, Zou
 * @version 1.0
 * @date 2022/2/10 10:29
 */
@Configuration
public class RestClientConfig {

    @Bean
    public RestHighLevelClient client(){
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("123.57.186.129", 9200, "http")));
        return client;
    }

}
