package com.novisign.slideshow.task.slideshow.controller;

import com.novisign.slideshow.task.slideshow.constant.ApiVersion;
import com.novisign.slideshow.task.slideshow.handler.SlideshowHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@AllArgsConstructor
public class SlideshowRoutes {

    private final ApiVersion apiVersion;
    private final SlideshowHandler slideshowHandler;

    @Bean
    public RouterFunction<ServerResponse> slideshowRouter() {
        return route()
                .nest(RequestPredicates.path("/api/" + apiVersion.getVersion()),
                        builder -> builder
                                .POST("/addSlideshow", slideshowHandler::addSlideshow)
                                .DELETE("/deleteSlideshow/{id}", slideshowHandler::deleteSlideshow)
                                .GET("/slideShow/{id}/slideshowOrder", slideshowHandler::slideshowOrder)
                                .POST("/slideShow/{id}/proof-of-play/{imageId}", slideshowHandler::proofOfPlay)
                )
                .build();
    }

}
