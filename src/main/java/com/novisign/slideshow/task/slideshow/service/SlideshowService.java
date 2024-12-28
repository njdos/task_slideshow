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
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.*;

@Service
@AllArgsConstructor
public class SlideshowService {

    private final DatabaseAPI databaseAPI;
    private final SlideShowProcessor slideShowProcessor;
    private final Validator validator;

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
                            .flatMap(slideshowId -> {
                                ApiResponse successResponse = ApiResponse.success(StatusCodes.SUCCESS,
                                        Collections.singletonList(Map.of("slideshowId", slideshowId)));
                                return Mono.just(successResponse);
                            })
                            .onErrorResume(ApiResponseUtils::ERROR_DATABASE_OPERATION_FAILED);
                });
    }

    private Mono<Long> validateAndSaveSlideshow(AddSlideshowRequest request, Map<Long, Integer> mappedImageIdAndDuration) {
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


    public Flux<ApiResponse> processSlideshowWithDelays(List<ApiResponse> responses) {
        List<Integer> durations = responses.stream()
                .map(this::extractDurationFromResponse)
                .toList();

        return Flux.fromIterable(responses)
                .index()
                .flatMapSequential(indexedElement -> {
                    long index = indexedElement.getT1();
                    ApiResponse response = indexedElement.getT2();

                    long delay = (index == 0) ? 0 : durations.get((int) (index - 1));

                    return Mono.delay(Duration.ofSeconds(delay))
                            .thenReturn(response);
                })
                .concatWith(
                        Mono.delay(Duration.ofSeconds(durations.get(durations.size() - 1)))
                                .then(Mono.just(ApiResponse.success(StatusCodes.OK,
                                        Collections.singletonList(Map.of("message", "End slideshow")))))
                );
    }

    private Integer extractDurationFromResponse(ApiResponse response) {
        return response.getData().stream()
                .findFirst()
                .flatMap(data -> Optional.of((Integer) ((Map<?, ?>) data).get("duration")))
                .orElse(0);
    }

}
