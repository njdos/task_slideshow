package com.novisign.slideshow.task.slideshow.validator;

import com.novisign.slideshow.task.slideshow.model.responce.ApiResponse;
import org.springframework.http.HttpHeaders;
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
                .flatMap(imageValidationResult -> {
                    if (imageValidationResult.getCode() != 200) {
                        return Mono.just(imageValidationResult);
                    }

                    return validateDuration(duration)
                            .flatMap(isDurationValid -> isDurationValid
                                    ? Mono.just(ApiResponse.success(200, imageValidationResult.getData()))
                                    : Mono.just(ApiResponse.error(1002))
                            );
                });
    }

    private Mono<ApiResponse> validateImage(Mono<ClientResponse> clientResponse) {
        return clientResponse.flatMap(response -> {
                    HttpHeaders headers = response.headers().asHttpHeaders();
                    List<String> contentTypeHeaders = headers.get("Content-Type");

                    return validateContentType(contentTypeHeaders)
                            .map(imageTypeOpt -> imageTypeOpt
                                    .map(type -> ApiResponse.success(200, List.of(Map.of("typeImage", type))))
                                    .orElse(ApiResponse.error(1001))
                            );
                })
                .onErrorResume(error -> Mono.just(ApiResponse.error(1000)));
    }

    private Mono<Optional<String>> validateContentType(List<String> headers) {
        if (headers == null || headers.isEmpty()) {
            return Mono.just(Optional.empty());
        }

        return Mono.just(
                headers.stream()
                        .filter(header -> header.startsWith("image/"))
                        .findFirst()
        );
    }

    private Mono<Boolean> validateDuration(int duration) {
        return Mono.just(duration > 0 && duration <= MAX_DURATION);
    }
}
