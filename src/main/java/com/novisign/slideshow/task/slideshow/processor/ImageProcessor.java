package com.novisign.slideshow.task.slideshow.processor;

import com.novisign.slideshow.task.slideshow.constant.StatusCodes;
import com.novisign.slideshow.task.slideshow.database.DatabaseAPI;
import com.novisign.slideshow.task.slideshow.entity.Image;
import com.novisign.slideshow.task.slideshow.exchange.Fetcher;
import com.novisign.slideshow.task.slideshow.exchange.RequestBuilder;
import com.novisign.slideshow.task.slideshow.model.AddImageRequest;
import com.novisign.slideshow.task.slideshow.model.ApiResponse;
import com.novisign.slideshow.task.slideshow.utils.ApiResponseUtils;
import com.novisign.slideshow.task.slideshow.utils.UrlUtils;
import com.novisign.slideshow.task.slideshow.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Component
public class ImageProcessor {

    @Autowired
    public ImageProcessor(Fetcher fetcher, Validator validator, UrlUtils urlUtils, DatabaseAPI databaseAPI) {
        this.fetcher = fetcher;
        this.validator = validator;
        this.urlUtils = urlUtils;
        this.databaseAPI = databaseAPI;
    }

    private final Fetcher fetcher;
    private final Validator validator;
    private final UrlUtils urlUtils;
    private final DatabaseAPI databaseAPI;

    public Mono<ApiResponse> processNewImage(AddImageRequest request) {
        RequestBuilder imageRequest = RequestBuilder.builder(request.url(), HttpMethod.HEAD);

        return fetcher.fetchRequest(imageRequest)
                .flatMap(clientResponse -> validator.validate(Mono.just(clientResponse), request.duration()))
                .flatMap(validationResult -> {
                    if (validationResult.getCode() != StatusCodes.OK.getCode()) {
                        return Mono.just(validationResult);
                    }

                    return saveValidatedImage(validationResult, request);
                });
    }

    private Mono<ApiResponse> saveValidatedImage(ApiResponse validationResult, AddImageRequest request) {
        var imageType = urlUtils.extractImageType(validationResult);
        if (imageType.isEmpty()) {
            return Mono.just(ApiResponse.error(StatusCodes.FAILED_VALIDATION_IMAGE));
        }

        List<String> keywords = urlUtils.extractKeywordsFromUrl(request.url());
        Image image = new Image(request.url(), request.duration(), imageType.get());

        return databaseAPI.saveNewImage(image, keywords)
                .map(savedImage -> ApiResponse.success(StatusCodes.SUCCESS, Collections.emptyList()))
                .onErrorResume(ApiResponseUtils::ERROR_DATABASE_OPERATION_FAILED);
    }
}
