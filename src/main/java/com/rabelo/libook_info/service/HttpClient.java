package com.rabelo.libook_info.service;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

public class HttpClient {
    private final RestClient restClient;

    public HttpClient() {
        this.restClient = RestClient.builder()
                .baseUrl("http://gutendex.com/books")
                .requestFactory(new HttpComponentsClientHttpRequestFactory())
                .build();
    }

    public <T> T getData(String addToAddress, Class<T> tClass) {
        return restClient.get()
                .uri("?search=" + addToAddress)
                .retrieve()
                .body(tClass);
    }
}
