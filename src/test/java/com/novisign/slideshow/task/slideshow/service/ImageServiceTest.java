package com.novisign.slideshow.task.slideshow.service;

import com.novisign.slideshow.task.slideshow.constant.StatusCodes;
import com.novisign.slideshow.task.slideshow.database.DatabaseAPI;
import com.novisign.slideshow.task.slideshow.entity.Image;
import com.novisign.slideshow.task.slideshow.model.AddImageRequest;
import com.novisign.slideshow.task.slideshow.model.ApiResponse;
import com.novisign.slideshow.task.slideshow.processor.ImageProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ImageServiceTest {

    @Mock
    private DatabaseAPI databaseAPI;

    @Mock
    private ImageProcessor imageProcessor;

    private ImageService imageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        imageService = new ImageService(databaseAPI, imageProcessor);
    }

    @Test
    void testAddImage_alreadyExists() {
        AddImageRequest request = new AddImageRequest("https://example.com/image.png", 10);
        when(databaseAPI.findByUrl(request.url())).thenReturn(Mono.just(new Image()));

        Mono<ApiResponse> result = imageService.addImage(request);

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getCode() == StatusCodes.ALREADY_EXISTS.getCode())
                .verifyComplete();

        verify(databaseAPI, times(1)).findByUrl(request.url());
        verifyNoInteractions(imageProcessor);
    }

    @Test
    void testAddImage_processNewImage() {
        AddImageRequest request = new AddImageRequest("https://example.com/image.png", 10);
        when(databaseAPI.findByUrl(request.url())).thenReturn(Mono.empty());
        ApiResponse successResponse = ApiResponse.success(StatusCodes.SUCCESS, null);
        when(imageProcessor.processNewImage(request)).thenReturn(Mono.just(successResponse));

        Mono<ApiResponse> result = imageService.addImage(request);

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getCode() == StatusCodes.SUCCESS.getCode())
                .verifyComplete();

        verify(databaseAPI, times(1)).findByUrl(request.url());
        verify(imageProcessor, times(1)).processNewImage(request);
    }

    @Test
    void testAddImage_databaseError() {
        AddImageRequest request = new AddImageRequest("https://example.com/image.png", 10);
        when(databaseAPI.findByUrl(request.url())).thenReturn(Mono.error(new RuntimeException("Database error")));

        Mono<ApiResponse> result = imageService.addImage(request);

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getCode() == StatusCodes.DATABASE_OPERATION_FAILED.getCode())
                .verifyComplete();

        verify(databaseAPI, times(1)).findByUrl(request.url());
        verifyNoInteractions(imageProcessor);
    }
}