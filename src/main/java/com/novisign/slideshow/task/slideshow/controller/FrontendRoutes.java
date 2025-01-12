package com.novisign.slideshow.task.slideshow.controller;

import com.novisign.slideshow.task.slideshow.handler.FrontendHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@AllArgsConstructor
public class FrontendRoutes {

    private final FrontendHandler frontendHandler;

    @Bean
    public RouterFunction<ServerResponse> uiRouter() {
        return route()
                .GET("/", frontendHandler::loadHtmlFile)
                .build();
    }

}
