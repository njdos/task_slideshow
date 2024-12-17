package com.novisign.slideshow.task.slideshow.exception;

import com.novisign.slideshow.task.slideshow.constant.StatusCodes;
import com.novisign.slideshow.task.slideshow.model.ApiResponse;
import com.novisign.slideshow.task.slideshow.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
@Order(-2)
public class GlobalExceptionHandler implements WebExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final JsonUtils jsonUtils;

    @Autowired
    public GlobalExceptionHandler(JsonUtils jsonUtils) {
        this.jsonUtils = jsonUtils;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ApiResponse response;

        String requestUrl = exchange.getRequest().getURI().toString();
        logger.info("Handling error for request URL: {}", requestUrl);

        if (ex instanceof CustomDatabaseException) {
            response = ApiResponse.error(StatusCodes.DATABASE_OPERATION_FAILED);
            logger.error("Database operation failed for request URL: {}. Error: {}", requestUrl, ex.getMessage(), ex);
        } else if (ex instanceof RuntimeException) {
            response = ApiResponse.error(StatusCodes.NOT_FOUND);
            logger.error("Runtime exception occurred for request URL: {}. Error: {}", requestUrl, ex.getMessage(), ex);
        } else {
            response = ApiResponse.error(StatusCodes.INTERNAL_SERVER_ERROR);
            logger.error("Unhandled exception for request URL: {}. Error: {}", requestUrl, ex.getMessage(), ex);
        }

        String jsonResponse = jsonUtils.toJson(response);

        exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.OK);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        byte[] bytes = jsonResponse.getBytes(StandardCharsets.UTF_8);

        // Возвращаем ответ с ошибкой
        return exchange.getResponse().writeWith(
                Mono.just(exchange.getResponse().bufferFactory().wrap(bytes))
        );
    }
}
