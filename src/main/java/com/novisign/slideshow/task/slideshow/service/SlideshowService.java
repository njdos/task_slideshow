package com.novisign.slideshow.task.slideshow.service;

import com.novisign.slideshow.task.slideshow.constant.StatusCodes;
import com.novisign.slideshow.task.slideshow.database.DatabaseAPI;
import com.novisign.slideshow.task.slideshow.model.AddSlideshowRequest;
import com.novisign.slideshow.task.slideshow.model.ApiResponse;
import com.novisign.slideshow.task.slideshow.model.TargetImageDuration;
import com.novisign.slideshow.task.slideshow.processor.SlideShowProcessor;
import com.novisign.slideshow.task.slideshow.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

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



        return databaseAPI.findImageByUrl(request.url())
                .hasElement()
                .flatMap(exists -> exists
                        ? Mono.just(ApiResponse.error(StatusCodes.ALREADY_EXISTS))
                        : slideShowProcessor.processNewImage(request)
                )
                .onErrorResume(error ->
                        Mono.just(ApiResponse.error(StatusCodes.DATABASE_OPERATION_FAILED))
                );
    }

    private M isValidAllImageDuration(List<TargetImageDuration> images) {
        for (TargetImageDuration image : images) {
            validator.validateDuration(image.duration())
        }
    }

}
