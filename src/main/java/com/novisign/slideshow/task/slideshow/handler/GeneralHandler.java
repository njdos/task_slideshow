package com.novisign.slideshow.task.slideshow.handler;

import com.novisign.slideshow.task.slideshow.constant.StatusCodes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class GeneralHandler {

    public Mono<ServerResponse> statusCodes(ServerRequest request) {
        Map<Integer, String> statusCodesMap = Arrays.stream(StatusCodes.values())
                .collect(Collectors.toMap(
                        StatusCodes::getCode,
                        StatusCodes::getMessage
                ));

        return ServerResponse.ok().bodyValue(statusCodesMap);
    }

    public Mono<ServerResponse> favicon(ServerRequest request) {
        return ServerResponse.status(HttpStatus.OK).build();
    }
}
