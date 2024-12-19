package com.novisign.slideshow.task.slideshow.handler;

import com.novisign.slideshow.task.slideshow.constant.StatusCodes;
import com.novisign.slideshow.task.slideshow.model.AddImageRequest;
import com.novisign.slideshow.task.slideshow.model.ApiResponse;
import com.novisign.slideshow.task.slideshow.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Component
public class ImageHandler {

    @Autowired
    public ImageHandler(ImageService imageService) {
        this.imageService = imageService;
    }

    private final ImageService imageService;

    public Mono<ServerResponse> addImage(ServerRequest request) {
        return request.bodyToMono(AddImageRequest.class)
                .flatMap(imageService::addImage)
                .flatMap(apiResponse -> {
                    HttpStatus status = apiResponse.getCode() == HttpStatus.CREATED.value()
                            ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;

                    return ServerResponse.status(status).bodyValue(apiResponse);
                })
                .onErrorResume(e -> ServerResponse.status(HttpStatus.BAD_REQUEST)
                        .bodyValue(ApiResponse.error(StatusCodes.INVALID_REQUEST_BODY)));
    }

    public Mono<ServerResponse> deleteImage(ServerRequest request) {
        return Mono.justOrEmpty(request.pathVariable("id"))
                .flatMap(id -> {
                    try {
                        Long imageId = Long.valueOf(id);
                        return imageService.deleteImageById(imageId);
                    } catch (NumberFormatException e) {
                        return Mono.just(ApiResponse.error(StatusCodes.INVALID_REQUEST_BODY));
                    }
                })
                .flatMap(apiResponse -> {
                    HttpStatus status = apiResponse.getCode() == HttpStatus.NO_CONTENT.value()
                            ? HttpStatus.NO_CONTENT
                            : HttpStatus.BAD_REQUEST;

                    return ServerResponse.status(status).bodyValue(apiResponse);
                })
                .onErrorResume(e -> ServerResponse.status(HttpStatus.BAD_REQUEST)
                        .bodyValue(ApiResponse.error(StatusCodes.INVALID_REQUEST_BODY)));
    }

    public Mono<ServerResponse> search(ServerRequest request) {
        String keyword = request.queryParam("keyword")
                .orElse("");
        Integer duration = request.queryParam("duration")
                .map(Integer::parseInt)
                .orElse(0);

        if (keyword.isEmpty() && duration == 0) {
            return ServerResponse.status(HttpStatus.OK)
                    .bodyValue(ApiResponse.success(StatusCodes.OK, Collections.emptyList()));
        }

        return imageService.search(keyword, duration)
                .flatMap(images ->
                        ServerResponse.ok()
                                .bodyValue(ApiResponse.success(StatusCodes.OK, images.getData()))
                                .onErrorResume(e -> ServerResponse.status(HttpStatus.BAD_REQUEST)
                                        .bodyValue(ApiResponse.error(StatusCodes.INVALID_REQUEST_PARAMETERS)))
                )
                .onErrorResume(e ->
                        ServerResponse.status(HttpStatus.BAD_REQUEST)
                                .bodyValue(ApiResponse.error(StatusCodes.INVALID_REQUEST_BODY)));
    }

}
