package com.novisign.slideshow.task.slideshow.exchange;

import com.novisign.slideshow.task.slideshow.config.HttpClientConfig;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
@AllArgsConstructor
public class Fetcher {

    private final WebClient.Builder webClientBuilder;
    private final RetryPolicy retryPolicy;


    public Mono<ClientResponse> fetchRequest(RequestBuilder request) {
        WebClient webClient = webClientBuilder.baseUrl(request.getUrl()).build();
        WebClient.RequestBodySpec requestBodySpec = configureRequest(request, webClient);

        return requestBodySpec.exchange()
                .timeout(Duration.ofSeconds(HttpClientConfig.REQUEST_TIMEOUT_SECONDS))
                .retryWhen(retryPolicy.createRetryPolicy());
    }

    private WebClient.RequestBodySpec configureRequest(RequestBuilder request, WebClient webClient) {
        WebClient.RequestBodySpec requestBodySpec = webClient
                .method(request.getMethod())
                .uri(request.getUrl());
        if (request.getHeaders() != null) {
            requestBodySpec.headers(httpHeaders -> httpHeaders.addAll(request.getHeaders()));
        }
        if (request.getBody() != null) {
            requestBodySpec.bodyValue(request.getBody());
        }
        return requestBodySpec;
    }
}
