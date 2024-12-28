package com.novisign.slideshow.task.slideshow.controller;

import com.novisign.slideshow.task.slideshow.handler.SwaggerHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@AllArgsConstructor
public class SwaggerRoutes {

    private final SwaggerHandler swaggerHandler;

    @Bean
    public RouterFunction<ServerResponse> swaggerRouter() {
        return route(GET("/swagger.yaml"), swaggerHandler::getSwaggerYaml);
    }

}
