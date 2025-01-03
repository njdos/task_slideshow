package com.novisign.slideshow.task.slideshow.exception;

import com.novisign.slideshow.task.slideshow.constant.StatusCodes;
import com.novisign.slideshow.task.slideshow.model.ApiResponse;
import com.novisign.slideshow.task.slideshow.utils.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
@Order(-2)
@AllArgsConstructor
@Slf4j
public class GlobalExceptionHandler implements WebExceptionHandler {

    private final JsonUtils jsonUtils;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ApiResponse response;

        String requestUrl = exchange.getRequest().getURI().toString();
        if (ex instanceof JsonSerializationException) {
            response = ApiResponse.error(StatusCodes.SERIALIZATION_ERROR);
            log.error("JSON serialization error for request URL: {}. Error: {}", requestUrl, ex.getMessage(), ex);
        } else if (ex instanceof CustomDatabaseException) {
            response = ApiResponse.error(StatusCodes.DATABASE_OPERATION_FAILED);
            log.error("Database operation failed for request URL: {}. Error: {}", requestUrl, ex.getMessage(), ex);
        } else if (ex instanceof DataMappingException) {
            response = ApiResponse.error(StatusCodes.MAPPING_ERROR);
            log.error("Data mapping error for request URL: {}. Error: {}", requestUrl, ex.getMessage(), ex);
        } else if (ex instanceof TransactionRollbackException) {
            response = ApiResponse.error(StatusCodes.DATABASE_OPERATION_FAILED);
            log.error("Transaction failed for request URL: {}. Error: {}", exchange.getRequest().getURI(), ex.getMessage(), ex);
        } else if (ex instanceof RuntimeException) {
            response = ApiResponse.error(StatusCodes.NOT_FOUND);
            log.error("Runtime exception occurred for request URL: {}. Error: {}", requestUrl, ex.getMessage(), ex);
        } else {
            response = ApiResponse.error(StatusCodes.INTERNAL_SERVER_ERROR);
            log.error("Unhandled exception for request URL: {}. Error: {}", requestUrl, ex.getMessage(), ex);
        }

        String jsonResponse = jsonUtils.toJson(response);

        exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.OK);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        byte[] bytes = jsonResponse.getBytes(StandardCharsets.UTF_8);

        return exchange.getResponse().writeWith(
                Mono.just(exchange.getResponse().bufferFactory().wrap(bytes))
        );
    }

}
