package com.novisign.slideshow.task.slideshow.handler;

import com.novisign.slideshow.task.slideshow.constant.StatusCodes;
import com.novisign.slideshow.task.slideshow.model.ApiResponse;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.nio.file.Files;

@Component
public class SwaggerHandler {

    public Mono<ServerResponse> getSwaggerYaml(ServerRequest request) {
        return Mono.fromCallable(() -> {
                    var resource = new ClassPathResource("swagger.yaml");
                    return Files.readString(resource.getFile().toPath());
                })
                .flatMap(content -> ServerResponse.ok()
                        .contentType(MediaType.TEXT_PLAIN)
                        .bodyValue(content))
                .onErrorResume(e -> ServerResponse.status(500)
                        .bodyValue(ApiResponse.error(StatusCodes.LOADING_FILE_FAILED)));
    }
}
