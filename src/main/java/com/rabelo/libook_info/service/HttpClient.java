package com.rabelo.libook_info.service;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

public class HttpClient {

    public static  <T> T getData(String addToAddress, Class<T> tClass) {
        var restClient = RestClient.builder()
                .baseUrl("http://gutendex.com/books")
                .requestFactory(new HttpComponentsClientHttpRequestFactory())
                .build();
        return restClient.get()
                .uri("?search=" + addToAddress)
                .retrieve()
                .body(tClass);
    }
}
