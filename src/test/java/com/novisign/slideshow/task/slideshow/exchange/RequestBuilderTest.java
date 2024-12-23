package com.novisign.slideshow.task.slideshow.exchange;


import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import static org.assertj.core.api.Assertions.assertThat;

class RequestBuilderTest {

    @Test
    void builder_ShouldCreateRequestBuilderWithUrlAndMethod() {
        String url = "http://example.com";
        HttpMethod method = HttpMethod.GET;

        RequestBuilder requestBuilder = RequestBuilder.builder(url, method);

        assertThat(requestBuilder).isNotNull();
        assertThat(requestBuilder.getUrl()).isEqualTo(url);
        assertThat(requestBuilder.getMethod()).isEqualTo(method);
    }

    @Test
    void headers_ShouldSetHeadersCorrectly() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer token");

        RequestBuilder requestBuilder = RequestBuilder.builder("http://example.com", HttpMethod.POST)
                .headers(headers);

        assertThat(requestBuilder.getHeaders()).isEqualTo(headers);
    }

    @Test
    void body_ShouldSetBodyCorrectly() {
        String body = "{\"key\":\"value\"}";

        RequestBuilder requestBuilder = RequestBuilder.builder("http://example.com", HttpMethod.POST)
                .body(body);

        assertThat(requestBuilder.getBody()).isEqualTo(body);
    }

}