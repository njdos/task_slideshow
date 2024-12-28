package com.novisign.slideshow.task.slideshow.controller;

import com.novisign.slideshow.task.slideshow.constant.ApiVersion;
import com.novisign.slideshow.task.slideshow.handler.ImageHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@AllArgsConstructor
public class ImageRoutes {

    private final ApiVersion apiVersion;
    private final ImageHandler imageHandler;

    @Bean
    public RouterFunction<ServerResponse> imageRouter() {
        return route()
                .nest(RequestPredicates.path("/api/" + apiVersion.getVersion()),
                        builder -> builder
                                .POST("/addImage", imageHandler::addImage)
                                .DELETE("/deleteImage/{id}", imageHandler::deleteImage)
                                .GET("/images/search", imageHandler::search)
                )
                .build();
    }

}
