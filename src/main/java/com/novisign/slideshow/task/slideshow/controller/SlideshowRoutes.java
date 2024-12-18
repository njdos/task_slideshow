package com.novisign.slideshow.task.slideshow.controller;

import com.novisign.slideshow.task.slideshow.constant.ApiVersion;
import com.novisign.slideshow.task.slideshow.handler.ImageHandler;
import com.novisign.slideshow.task.slideshow.handler.SlideshowHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class SlideshowRoutes {

    @Autowired
    public SlideshowRoutes(ApiVersion apiVersion, SlideshowHandler slideshowHandler) {
        this.apiVersion = apiVersion;
        this.slideshowHandler = slideshowHandler;
    }

    private final ApiVersion apiVersion;
    private final SlideshowHandler slideshowHandler;

    @Bean
    public RouterFunction<ServerResponse> imageRouter() {
        return route()
                .nest(RequestPredicates.path("/api/" + apiVersion.getVersion()),
                        builder -> builder
                                .POST("/addSlideshow", slideshowHandler::addSlideshow)
                                .DELETE ("/deleteSlideshow/{id}", slideshowHandler::deleteSlideshow)
                )
                .build();
    }

}
