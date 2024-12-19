package com.novisign.slideshow.task.slideshow.controller;

import com.novisign.slideshow.task.slideshow.handler.SwaggerHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class SwaggerRoutes {

    @Autowired
    public SwaggerRoutes(SwaggerHandler swaggerHandler) {
        this.swaggerHandler = swaggerHandler;
    }

    private final SwaggerHandler swaggerHandler;

    @Bean
    public RouterFunction<ServerResponse> swaggerRouter() {
        return route(GET("/swagger.yaml"), swaggerHandler::getSwaggerYaml);
    }

}
