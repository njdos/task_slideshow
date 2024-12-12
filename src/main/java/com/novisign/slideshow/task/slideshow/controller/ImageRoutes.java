package com.novisign.slideshow.task.slideshow.controller;

import com.novisign.slideshow.task.slideshow.constants.ApiVersion;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ImageRoutes {

    @Bean
    public RouterFunction<ServerResponse> imageRoutes() {
        return route()
                .nest(RequestPredicates.path("/api" + ApiVersion.VERSION_1),
                        builder -> builder
                                .POST("/addImage", this::addImage)
                                .DELETE("/deleteImage/{id}", this::deleteImage)
                                .GET("/images/search", this::searchImages)
                )
                .build();
    }


    private Mono<ServerResponse> addImage(ServerRequest serverRequest) {
        return null;
    }

    private Mono<ServerResponse> deleteImage(ServerRequest serverRequest) {
        return null;
    }

    private Mono<ServerResponse> searchImages(ServerRequest serverRequest) {
        return null;
    }
}
