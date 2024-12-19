package com.novisign.slideshow.task.slideshow.service;

import com.novisign.slideshow.task.slideshow.constant.StatusCodes;
import com.novisign.slideshow.task.slideshow.database.DatabaseAPI;
import com.novisign.slideshow.task.slideshow.model.AddImageRequest;
import com.novisign.slideshow.task.slideshow.model.ApiResponse;
import com.novisign.slideshow.task.slideshow.processor.ImageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class ImageService {

    private final DatabaseAPI databaseAPI;
    private final ImageProcessor imageProcessor;

    @Autowired
    public ImageService(DatabaseAPI databaseAPI, ImageProcessor imageProcessor) {
        this.databaseAPI = databaseAPI;
        this.imageProcessor =    imageProcessor;
    }

    public Mono<ApiResponse> addImage(AddImageRequest request) {
        return databaseAPI.findImageByUrl(request.url())
                .hasElement()
                .flatMap(exists -> exists
                        ? Mono.just(ApiResponse.error(StatusCodes.ALREADY_EXISTS))
                        : imageProcessor.processNewImage(request)
                )
                .onErrorResume(error ->
                        Mono.just(ApiResponse.error(StatusCodes.DATABASE_OPERATION_FAILED))
                );
    }

    public Mono<ApiResponse> deleteImageById(Long id) {
        return databaseAPI.deleteImageById(id)
                .flatMap(deleted -> deleted
                        ? Mono.just(ApiResponse.success(StatusCodes.SUCCESS, Collections.emptyList()))
                        : Mono.just(ApiResponse.error(StatusCodes.NOT_FOUND)))
                .onErrorResume(error -> Mono.just(ApiResponse.error(StatusCodes.DATABASE_OPERATION_FAILED)));
    }

    public Mono<ApiResponse> search(String keyword, Integer duration) {
        return databaseAPI.search(keyword, duration);
    }
}
