package com.novisign.slideshow.task.slideshow.handler;

import com.novisign.slideshow.task.slideshow.constant.StatusCodes;
import com.novisign.slideshow.task.slideshow.model.AddSlideshowRequest;
import com.novisign.slideshow.task.slideshow.model.ApiResponse;
import com.novisign.slideshow.task.slideshow.service.SlideshowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class SlideshowHandler {

    @Autowired
    public SlideshowHandler(SlideshowService slideshowService) {
        this.slideshowService = slideshowService;
    }

    private final SlideshowService slideshowService;

    public Mono<ServerResponse> addSlideshow(ServerRequest request) {
        return request.bodyToMono(AddSlideshowRequest.class)
                .flatMap(slideshowService::addSlideshow)
                .flatMap(apiResponse -> {
                    HttpStatus status = apiResponse.getCode() == HttpStatus.CREATED.value()
                            ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;

                    return ServerResponse.status(status).bodyValue(apiResponse);
                })
                .onErrorResume(e -> ServerResponse.status(HttpStatus.BAD_REQUEST)
                        .bodyValue(ApiResponse.error(StatusCodes.INVALID_REQUEST)));
    }

    public Mono<ServerResponse> deleteSlideshow(ServerRequest request) {
        return Mono.justOrEmpty(request.pathVariable("id"))
                .flatMap(id -> {
                    try {
                        Long imageId = Long.valueOf(id);
                        return slideshowService.deleteSlideshowById(imageId);
                    } catch (NumberFormatException e) {
                        return Mono.just(ApiResponse.error(StatusCodes.INVALID_REQUEST));
                    }
                })
                .flatMap(apiResponse -> {
                    HttpStatus status = apiResponse.getCode() == HttpStatus.NO_CONTENT.value()
                            ? HttpStatus.NO_CONTENT
                            : HttpStatus.BAD_REQUEST;

                    return ServerResponse.status(status).bodyValue(apiResponse);
                })
                .onErrorResume(e -> ServerResponse.status(HttpStatus.BAD_REQUEST)
                        .bodyValue(ApiResponse.error(StatusCodes.INVALID_REQUEST)));
    }

}
