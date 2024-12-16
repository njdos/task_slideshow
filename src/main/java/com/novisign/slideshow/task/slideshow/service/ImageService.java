package com.novisign.slideshow.task.slideshow.service;

import com.novisign.slideshow.task.slideshow.constant.StatusCodes;
import com.novisign.slideshow.task.slideshow.database.DatabaseAPI;
import com.novisign.slideshow.task.slideshow.model.AddImageRequest;
import com.novisign.slideshow.task.slideshow.model.ApiResponse;
import com.novisign.slideshow.task.slideshow.processor.ImageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

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
        return databaseAPI.findByUrl(request.url())
                .hasElement()
                .flatMap(exists -> exists
                        ? Mono.just(ApiResponse.error(StatusCodes.ALREADY_EXISTS))
                        : imageProcessor.processNewImage(request)
                )
                .onErrorResume(error -> Mono.just(ApiResponse.error(StatusCodes.DATABASE_OPERATION_FAILED)));
    }

}
