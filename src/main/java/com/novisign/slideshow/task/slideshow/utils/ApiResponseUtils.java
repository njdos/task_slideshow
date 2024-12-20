package com.novisign.slideshow.task.slideshow.utils;

import com.novisign.slideshow.task.slideshow.constant.StatusCodes;
import com.novisign.slideshow.task.slideshow.model.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class ApiResponseUtils {

    public static Mono<? extends ServerResponse> BAD_REQUEST_INVALID_REQUEST_BODY(Throwable throwable) {
        return ServerResponse.status(HttpStatus.BAD_REQUEST)
                .bodyValue(ApiResponse.error(StatusCodes.INVALID_REQUEST_BODY));
    }

    public static Mono<? extends ServerResponse> BAD_REQUEST_INVALID_REQUEST_PARAMETERS(Throwable throwable) {
        return ServerResponse.status(HttpStatus.BAD_REQUEST)
                .bodyValue(ApiResponse.error(StatusCodes.INVALID_REQUEST_PARAMETERS));
    }

    public static Mono<? extends ApiResponse> ERROR_DATABASE_OPERATION_FAILED(Throwable throwable) {
        return Mono.just(ApiResponse.error(StatusCodes.DATABASE_OPERATION_FAILED));
    }

}
