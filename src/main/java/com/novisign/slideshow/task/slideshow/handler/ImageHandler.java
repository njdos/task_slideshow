package com.novisign.slideshow.task.slideshow.handler;

import com.novisign.slideshow.task.slideshow.constant.StatusCodes;
import com.novisign.slideshow.task.slideshow.model.AddImageRequest;
import com.novisign.slideshow.task.slideshow.model.ApiResponse;
import com.novisign.slideshow.task.slideshow.model.SearchRequest;
import com.novisign.slideshow.task.slideshow.service.ImageService;
import com.novisign.slideshow.task.slideshow.utils.ApiResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

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
                .onErrorResume(ApiResponseUtils::BAD_REQUEST_INVALID_REQUEST_BODY);
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
                    HttpStatus status = apiResponse.getCode() == HttpStatus.CREATED.value()
                            ? HttpStatus.CREATED
                            : HttpStatus.BAD_REQUEST;

                    return ServerResponse.status(status).bodyValue(apiResponse);
                })
                .onErrorResume(ApiResponseUtils::BAD_REQUEST_INVALID_REQUEST_BODY);
    }

    public Mono<ServerResponse> search(ServerRequest request) {
        return request.bodyToMono(SearchRequest.class)
                .flatMap(imageService::search)
                .flatMap(images ->
                        ServerResponse.ok()
                                .bodyValue(ApiResponse.success(StatusCodes.OK, images.getData()))
                                .onErrorResume(ApiResponseUtils::BAD_REQUEST_INVALID_REQUEST_PARAMETERS)
                )
                .onErrorResume(ApiResponseUtils::BAD_REQUEST_INVALID_REQUEST_BODY);
    }

}
