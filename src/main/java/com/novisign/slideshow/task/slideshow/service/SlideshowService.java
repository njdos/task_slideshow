package com.novisign.slideshow.task.slideshow.service;

import com.novisign.slideshow.task.slideshow.constant.StatusCodes;
import com.novisign.slideshow.task.slideshow.database.DatabaseAPI;
import com.novisign.slideshow.task.slideshow.entity.Image;
import com.novisign.slideshow.task.slideshow.entity.ProofOfPlay;
import com.novisign.slideshow.task.slideshow.model.AddSlideshowRequest;
import com.novisign.slideshow.task.slideshow.model.ApiResponse;
import com.novisign.slideshow.task.slideshow.processor.SlideShowProcessor;
import com.novisign.slideshow.task.slideshow.utils.ApiResponseUtils;
import com.novisign.slideshow.task.slideshow.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SlideshowService {

    private final DatabaseAPI databaseAPI;
    private final SlideShowProcessor slideShowProcessor;
    private final Validator validator;

    @Autowired
    public SlideshowService(DatabaseAPI databaseAPI, SlideShowProcessor slideShowProcessor, Validator validator) {
        this.databaseAPI = databaseAPI;
        this.slideShowProcessor = slideShowProcessor;
        this.validator = validator;
    }

    public Mono<ApiResponse> addSlideshow(AddSlideshowRequest request) {
        return slideShowProcessor.isValidRequest(request)
                .flatMap(response -> {
                    if (response.getCode() != StatusCodes.OK.getCode()) {
                        return Mono.just(response);
                    }

                    List<Long> ids = (List<Long>) response.getData()
                            .get(0)
                            .get("ids");

                    return databaseAPI.findImageIdAndDurationByIds(ids)
                            .collectMap(Image::getId, Image::getDuration)
                            .flatMap(mappedImageIdAndDuration -> validateAndSaveSlideshow(request, mappedImageIdAndDuration))
                            .map(saved -> ApiResponse.success(StatusCodes.SUCCESS, Collections.emptyList()))
                            .onErrorResume(ApiResponseUtils::ERROR_DATABASE_OPERATION_FAILED);
                });
    }

    private Mono<Boolean> validateAndSaveSlideshow(AddSlideshowRequest request, Map<Long, Integer> mappedImageIdAndDuration) {
        return Flux.fromIterable(request.images())
                .flatMap(image -> {
                    boolean isExistImage = mappedImageIdAndDuration.containsKey(image.image_id());
                    if (!isExistImage) {
                        return Mono.just(false);
                    }

                    return validator.validateDuration(image.duration())
                            .flatMap(isValidDuration -> {
                                int duration = isValidDuration
                                        ? image.duration() : mappedImageIdAndDuration.get(image.image_id());
                                return Mono.just(Map.entry(image.image_id(), duration));
                            });
                })
                .collectList()
                .flatMap(entries -> {
                    Map<Long, Integer> correctSlideshow = new HashMap<>();

                    for (Object obj : entries) {
                        Map.Entry<Long, Integer> entry = (Map.Entry<Long, Integer>) obj;
                        correctSlideshow.put(entry.getKey(), entry.getValue());
                    }

                    return databaseAPI.saveSlideshow(request.name(), correctSlideshow);
                });
    }

    public Mono<ApiResponse> deleteSlideshowById(Long id) {
        return databaseAPI.deleteSlideshowById(id)
                .flatMap(deleted -> deleted
                        ? Mono.just(ApiResponse.success(StatusCodes.SUCCESS, Collections.emptyList()))
                        : Mono.just(ApiResponse.error(StatusCodes.NOT_FOUND)))
                .onErrorResume(ApiResponseUtils::ERROR_DATABASE_OPERATION_FAILED);
    }

    public Flux<ApiResponse> slideshowOrder(Long id) {
        return databaseAPI.slideshowOrder(id)
                .onErrorResume(ApiResponseUtils::ERROR_DATABASE_OPERATION_FAILED);
    }

    public Mono<ApiResponse> saveProofOfPlay(ProofOfPlay proofOfPlay) {
        return databaseAPI.saveProofOfPlay(proofOfPlay)
                .flatMap(generatedId -> {
                    if (generatedId <= 0) return ApiResponseUtils.ERROR_DATABASE_OPERATION_FAILED(null);

                    return Mono.just(ApiResponse.success(StatusCodes.OK, Collections.emptyList()));
                })
                .onErrorResume(ApiResponseUtils::ERROR_DATABASE_OPERATION_FAILED);
    }
}
