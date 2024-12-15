package com.novisign.slideshow.task.slideshow.validator;

import com.novisign.slideshow.task.slideshow.constant.ErrorCodes;
import com.novisign.slideshow.task.slideshow.model.ApiResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class ImageValidator {

    private static final int MAX_DURATION = 3600;

    public Mono<ApiResponse> validate(Mono<ClientResponse> clientResponse, int duration) {
        return validateImage(clientResponse)
                .flatMap(validationResponse -> validationResponse.getCode() == 200
                        ? validateDuration(duration, validationResponse)
                        : Mono.just(validationResponse)
                )
                .onErrorResume(throwable -> Mono.just(ApiResponse.error(ErrorCodes.FAILED_VALIDATION)));
    }

    private Mono<ApiResponse> validateImage(Mono<ClientResponse> clientResponse) {
        return clientResponse.flatMap(this::processClientResponse)
                .onErrorResume(throwable -> Mono.just(ApiResponse.error(ErrorCodes.FAILED_VALIDATION)));
    }

    private Mono<ApiResponse> processClientResponse(ClientResponse response) {
        List<String> contentTypeHeaders = response.headers().asHttpHeaders().get("Content-Type");

        return validateContentType(contentTypeHeaders)
                .map(contentTypeOpt -> contentTypeOpt
                        .map(type -> ApiResponse.success(ErrorCodes.SUCCESS, List.of(Map.of("typeImage", type))))
                        .orElse(ApiResponse.error(ErrorCodes.FAILED_VALIDATION_IMAGE))
                );
    }

    private Mono<Optional<String>> validateContentType(List<String> headers) {
        if (headers == null || headers.isEmpty()) {
            return Mono.just(Optional.empty());
        }

        return Mono.just(headers.stream()
                .filter(header -> header.startsWith("image/"))
                .findFirst()
        );
    }

    private Mono<ApiResponse> validateDuration(int duration, ApiResponse validationResponse) {
        boolean isValidDuration = duration > 0 && duration <= MAX_DURATION;
        return isValidDuration
                ? Mono.just(ApiResponse.success(ErrorCodes.OK, validationResponse.getData()))
                : Mono.just(ApiResponse.error(ErrorCodes.FAILED_VALIDATION_DURATION));
    }
}
