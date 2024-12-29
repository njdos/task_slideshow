package com.novisign.slideshow.task.slideshow.service;

import com.novisign.slideshow.task.slideshow.constant.StatusCodes;
import com.novisign.slideshow.task.slideshow.database.DatabaseAPI;
import com.novisign.slideshow.task.slideshow.database.helper.BindConfigurer;
import com.novisign.slideshow.task.slideshow.entity.Image;
import com.novisign.slideshow.task.slideshow.kafka.KafkaAPI;
import com.novisign.slideshow.task.slideshow.model.AddImageRequest;
import com.novisign.slideshow.task.slideshow.model.ApiResponse;
import com.novisign.slideshow.task.slideshow.model.SearchRequest;
import com.novisign.slideshow.task.slideshow.processor.ImageProcessor;
import com.novisign.slideshow.task.slideshow.utils.ImageUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class ImageServiceTest {

    @Mock
    private DatabaseAPI databaseAPI;

    @Mock
    private ImageProcessor imageProcessor;

    @Mock
    private ImageUtils utils;

    @Mock
    private KafkaAPI kafkaAPI;

    private ImageService imageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        imageService = new ImageService(kafkaAPI, databaseAPI, imageProcessor, utils);
    }

    @Test
    void testAddImage_alreadyExists() {
        AddImageRequest request = new AddImageRequest("https://example.com/image.png", 10);
        when(databaseAPI.findImageByUrl(request.url())).thenReturn(Mono.just(new Image()));

        Mono<ApiResponse> result = imageService.addImage(request);

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getCode() == StatusCodes.ALREADY_EXISTS.getCode())
                .verifyComplete();

        verify(databaseAPI, times(1)).findImageByUrl(request.url());
        verifyNoInteractions(imageProcessor);
    }

    @Test
    void testAddImage_databaseError() {
        AddImageRequest request = new AddImageRequest("https://example.com/image.png", 10);
        when(databaseAPI.findImageByUrl(request.url())).thenReturn(Mono.error(new RuntimeException("Database error")));

        Mono<ApiResponse> result = imageService.addImage(request);

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getCode() == StatusCodes.DATABASE_OPERATION_FAILED.getCode())
                .verifyComplete();

        verify(databaseAPI, times(1)).findImageByUrl(request.url());
        verifyNoInteractions(imageProcessor);
    }

    @Test
    void testDeleteImageById_notFound() {
        Long imageId = 1L;
        when(databaseAPI.deleteImageById(imageId)).thenReturn(Mono.just(false));

        Mono<ApiResponse> result = imageService.deleteImageById(imageId);

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getCode() == StatusCodes.NOT_FOUND.getCode())
                .verifyComplete();

        verify(databaseAPI, times(1)).deleteImageById(imageId);
    }

    @Test
    void testDeleteImageById_databaseError() {
        Long imageId = 1L;
        when(databaseAPI.deleteImageById(imageId)).thenReturn(Mono.error(new RuntimeException("Database error")));

        Mono<ApiResponse> result = imageService.deleteImageById(imageId);

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getCode() == StatusCodes.DATABASE_OPERATION_FAILED.getCode())
                .verifyComplete();

        verify(databaseAPI, times(1)).deleteImageById(imageId);
    }

    @Test
    void testSearch_success() {
        SearchRequest searchRequest = new SearchRequest("keyword", 10);
        String query = "SELECT * FROM images WHERE keyword = :keyword";
        BindConfigurer bindConfigurer = mock(BindConfigurer.class);

        when(utils.createSearchQuery(searchRequest)).thenReturn(Mono.just(query));
        when(utils.bindConfigurer(searchRequest)).thenReturn(bindConfigurer);
        when(databaseAPI.search(any(), any())).thenReturn(Mono.just(ApiResponse.success(StatusCodes.SUCCESS, null)));

        Mono<ApiResponse> result = imageService.search(searchRequest);

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getCode() == StatusCodes.SUCCESS.getCode())
                .verifyComplete();

        verify(databaseAPI, times(1)).search(any(), any());
    }

    @Test
    void testSearch_emptyQuery() {
        SearchRequest searchRequest = new SearchRequest("keyword", 10);
        when(utils.createSearchQuery(searchRequest)).thenReturn(Mono.just(""));

        Mono<ApiResponse> result = imageService.search(searchRequest);

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getCode() == StatusCodes.INVALID_REQUEST_BODY.getCode())
                .verifyComplete();

        verifyNoInteractions(databaseAPI);
    }

    @Test
    void testSearch_error() {
        SearchRequest searchRequest = new SearchRequest("keyword", 10);
        when(utils.createSearchQuery(searchRequest)).thenReturn(Mono.just("SELECT * FROM images"));
        when(utils.bindConfigurer(searchRequest)).thenReturn(mock(BindConfigurer.class));
        when(databaseAPI.search(any(), any())).thenReturn(Mono.error(new RuntimeException("Database error")));

        Mono<ApiResponse> result = imageService.search(searchRequest);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException && throwable.getMessage().contains("Database error"))
                .verify();

        verify(databaseAPI, times(1)).search(any(), any());
    }
}
