package com.novisign.slideshow.task.slideshow.exchange;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

public class RequestBuilder {

    private final String url;
    private final HttpMethod method;
    private HttpHeaders headers;
    private Object body;

    private RequestBuilder(String url, HttpMethod method) {
        this.url = url;
        this.method = method;
    }

    public static RequestBuilder builder(String url, HttpMethod method) {
        return new RequestBuilder(url, method);
    }

    public RequestBuilder headers(HttpHeaders headers) {
        this.headers = headers;
        return this;
    }

    public RequestBuilder body(Object body) {
        this.body = body;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public Object getBody() {
        return body;
    }
}