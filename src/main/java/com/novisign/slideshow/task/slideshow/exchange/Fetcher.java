package com.novisign.slideshow.task.slideshow.exchange;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class Fetcher {

    @Autowired
    public Fetcher(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    private final WebClient.Builder webClientBuilder;


    public Mono<ClientResponse> fetchRequest(RequestBuilder request) {
        WebClient webClient = webClientBuilder.baseUrl(request.getUrl()).build();
        WebClient.RequestBodySpec requestBodySpec = webClient.method(request.getMethod()).uri(request.getUrl());

        if (request.getHeaders() != null) {
            requestBodySpec.headers(httpHeaders -> httpHeaders.addAll(request.getHeaders()));
        }

        if (request.getBody() != null) {
            requestBodySpec.bodyValue(request.getBody());
        }

        return requestBodySpec.exchange();
    }
}
