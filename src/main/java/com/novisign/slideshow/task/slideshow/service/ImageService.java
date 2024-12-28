package com.novisign.slideshow.task.slideshow.service;

import com.novisign.slideshow.task.slideshow.constant.StatusCodes;
import com.novisign.slideshow.task.slideshow.database.DatabaseAPI;
import com.novisign.slideshow.task.slideshow.database.helper.BindConfigurer;
import com.novisign.slideshow.task.slideshow.database.queryMapping.DynamicQueryMapping;
import com.novisign.slideshow.task.slideshow.mapper.Mapper;
import com.novisign.slideshow.task.slideshow.model.AddImageRequest;
import com.novisign.slideshow.task.slideshow.model.ApiResponse;
import com.novisign.slideshow.task.slideshow.model.SearchRequest;
import com.novisign.slideshow.task.slideshow.processor.ImageProcessor;
import com.novisign.slideshow.task.slideshow.utils.ApiResponseUtils;
import com.novisign.slideshow.task.slideshow.utils.ImageUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Service
@AllArgsConstructor
public class ImageService {

    private final DatabaseAPI databaseAPI;
    private final ImageProcessor imageProcessor;
    private final ImageUtils imageUtils;

    public Mono<ApiResponse> addImage(AddImageRequest request) {
        return databaseAPI.findImageByUrl(request.url())
                .hasElement()
                .flatMap(exists -> exists
                        ? Mono.just(ApiResponse.error(StatusCodes.ALREADY_EXISTS))
                        : imageProcessor.processNewImage(request)
                )
                .onErrorResume(ApiResponseUtils::ERROR_DATABASE_OPERATION_FAILED);
    }

    public Mono<ApiResponse> deleteImageById(Long id) {
        return databaseAPI.deleteImageById(id)
                .flatMap(deleted -> deleted
                        ? Mono.just(ApiResponse.success(StatusCodes.SUCCESS, Collections.emptyList()))
                        : Mono.just(ApiResponse.error(StatusCodes.NOT_FOUND)))
                .onErrorResume(ApiResponseUtils::ERROR_DATABASE_OPERATION_FAILED);
    }

    public Mono<ApiResponse> search(SearchRequest searchRequest) {
        Mono<String> searchQuery = imageUtils.createSearchQuery(searchRequest);

        BindConfigurer bindConfigurer = imageUtils.bindConfigurer(searchRequest);

        return searchQuery.flatMap(query -> {
            if (query == null || query.isEmpty()) {
                return Mono.just(ApiResponse.error(StatusCodes.INVALID_REQUEST_BODY));
            }

            var queryMapping = new DynamicQueryMapping(query, Mapper.mapRowToId);
            return databaseAPI.search(queryMapping, bindConfigurer);
        });
    }

}
