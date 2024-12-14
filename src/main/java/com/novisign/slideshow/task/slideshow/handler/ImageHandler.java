package com.novisign.slideshow.task.slideshow.handler;

import com.novisign.slideshow.task.slideshow.model.request.AddImageRequest;
import com.novisign.slideshow.task.slideshow.model.responce.ApiResponse;
import com.novisign.slideshow.task.slideshow.service.image.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class ImageHandler {
    public ImageHandler(ImageService imageService) {
        this.imageService = imageService;
    }

    private final ImageService imageService;

    public Mono<ServerResponse> addImage(ServerRequest request) {
        return request.bodyToMono(AddImageRequest.class)
                .log()
                .flatMap(imageService::addImage)
                .flatMap(apiResponse -> {
                    HttpStatus status = apiResponse.getCode() == HttpStatus.CREATED.value()
                            ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;

                    return ServerResponse.status(status).bodyValue(apiResponse);
                })
                .onErrorResume(e -> ServerResponse.status(HttpStatus.BAD_REQUEST)
                        .bodyValue(ApiResponse.error(1000)));
    }

}
