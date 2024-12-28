package com.novisign.slideshow.task.slideshow.controller;

import com.novisign.slideshow.task.slideshow.handler.GeneralHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@AllArgsConstructor
public class GeneralRoutes {

    private final GeneralHandler generalHandler;

    @Bean
    public RouterFunction<ServerResponse> staticResourcesRouter() {
        return route()
                .GET("/statusCodes", generalHandler::statusCodes)
                .GET("/favicon.ico", generalHandler::favicon)
                .build();
    }

}
