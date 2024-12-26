package com.novisign.slideshow.task.slideshow.handler;

import com.novisign.slideshow.task.slideshow.constant.StatusCodes;
import com.novisign.slideshow.task.slideshow.entity.ProofOfPlay;
import com.novisign.slideshow.task.slideshow.model.AddSlideshowRequest;
import com.novisign.slideshow.task.slideshow.model.ApiResponse;
import com.novisign.slideshow.task.slideshow.service.SlideshowService;
import com.novisign.slideshow.task.slideshow.utils.ApiResponseUtils;
import com.novisign.slideshow.task.slideshow.utils.ConverterUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Component
public class SlideshowHandler {

    @Autowired
    public SlideshowHandler(SlideshowService slideshowService, ConverterUtils converterUtils) {
        this.slideshowService = slideshowService;
        this.converterUtils = converterUtils;
    }

    private final SlideshowService slideshowService;
    private final ConverterUtils converterUtils;

    public Mono<ServerResponse> addSlideshow(ServerRequest request) {
        return request.bodyToMono(AddSlideshowRequest.class)
                .flatMap(slideshowService::addSlideshow)
                .flatMap(apiResponse -> {
                    HttpStatus status = apiResponse.getCode() == HttpStatus.CREATED.value()
                            ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;

                    return ServerResponse.status(status).bodyValue(apiResponse);
                })
                .onErrorResume(ApiResponseUtils::BAD_REQUEST_INVALID_REQUEST_BODY);
    }

    public Mono<ServerResponse> deleteSlideshow(ServerRequest request) {
        return Mono.justOrEmpty(request.pathVariable("id"))
                .flatMap(id -> {
                    try {
                        Long imageId = Long.valueOf(id);
                        return slideshowService.deleteSlideshowById(imageId);
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
                .onErrorResume(ApiResponseUtils::BAD_REQUEST_INVALID_REQUEST_BODY);
    }

    public Mono<ServerResponse> slideshowOrder(ServerRequest request) {
        return converterUtils.parseToLong(request.pathVariable("id"))
                .flatMapMany(slideshowService::slideshowOrder)
                .onErrorResume(e -> Flux.just(ApiResponse.error(StatusCodes.INVALID_REQUEST_BODY)))
                .collectList()
                .flatMapMany(slideshowService::processSlideshowWithDelays)
                .as(flux -> ServerResponse.ok()
                        .contentType(MediaType.TEXT_EVENT_STREAM)
                        .body(flux, ApiResponse.class));
    }


    public Mono<ServerResponse> proofOfPlay(ServerRequest request) {
        return converterUtils.parseToLong(request.pathVariable("id"))
                .zipWith(converterUtils.parseToLong(request.pathVariable("imageId")))
                .flatMap(ids -> {
                    Long slideshowId = ids.getT1();
                    Long imageId = ids.getT2();
                    ProofOfPlay proofOfPlay = new ProofOfPlay(slideshowId, imageId);

                    return slideshowService.saveProofOfPlay(proofOfPlay)
                            .flatMap(apiResponse -> ServerResponse.ok().bodyValue(
                                    ApiResponse.success(StatusCodes.SUCCESS, Collections.emptyList())));
                })
                .onErrorResume(ApiResponseUtils::BAD_REQUEST_INVALID_REQUEST_BODY);
    }

}
