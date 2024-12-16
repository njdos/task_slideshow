package com.novisign.slideshow.task.slideshow.processor;

import com.novisign.slideshow.task.slideshow.constant.StatusCodes;
import com.novisign.slideshow.task.slideshow.database.DatabaseAPI;
import com.novisign.slideshow.task.slideshow.entity.Image;
import com.novisign.slideshow.task.slideshow.exchange.Fetcher;
import com.novisign.slideshow.task.slideshow.exchange.RequestBuilder;
import com.novisign.slideshow.task.slideshow.model.AddImageRequest;
import com.novisign.slideshow.task.slideshow.model.ApiResponse;
import com.novisign.slideshow.task.slideshow.utils.UrlUtils;
import com.novisign.slideshow.task.slideshow.validator.ImageValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

class ImageProcessorTest {

    private ImageProcessor imageProcessor;

    @Mock
    private Fetcher mockFetcher;
    @Mock
    private ImageValidator mockImageValidator;
    @Mock
    private UrlUtils mockUrlUtils;
    @Mock
    private DatabaseAPI mockDatabaseAPI;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        imageProcessor = new ImageProcessor(mockFetcher, mockImageValidator, mockUrlUtils, mockDatabaseAPI);
    }

    @Test
    void testProcessNewImage_successfulProcessing() {
        String imageUrl = "https://fastly.picsum.photos/id/474/200/200.jpg?hmac=X5gJb746aYb_1-VdQG2Cti4XcHC10gwaOfRGfs6fTNk";
        int duration = 3000;
        AddImageRequest request = new AddImageRequest(imageUrl, duration);

        var mockClientResponse = mock(ClientResponse.class);
        when(mockFetcher.fetchRequest(any(RequestBuilder.class))).thenReturn(Mono.just(mockClientResponse));

        ApiResponse validationResponse = ApiResponse.success(StatusCodes.OK, Collections.emptyList());
        when(mockImageValidator.validate(any(Mono.class), eq(duration))).thenReturn(Mono.just(validationResponse));

        when(mockUrlUtils.extractImageType(validationResponse)).thenReturn(java.util.Optional.of("image/png"));
        when(mockUrlUtils.extractKeywordsFromUrl(imageUrl)).thenReturn(List.of("keyword1", "keyword2"));

        when(mockDatabaseAPI.saveNewImage(any(Image.class), anyList())).thenReturn(Mono.just(true));

        Mono<ApiResponse> responseMono = imageProcessor.processNewImage(request);

        StepVerifier.create(responseMono)
                .expectNextMatches(response ->
                        response.getCode() == StatusCodes.SUCCESS.getCode()
                )
                .verifyComplete();
    }

    @Test
    void testProcessNewImage_invalidContentType() {
        String imageUrl = "http://example.com/image.png";
        int duration = 3000;
        AddImageRequest request = new AddImageRequest(imageUrl, duration);

        var mockClientResponse = mock(ClientResponse.class);
        when(mockFetcher.fetchRequest(any(RequestBuilder.class))).thenReturn(Mono.just(mockClientResponse));

        ApiResponse validationResponse = ApiResponse.error(StatusCodes.FAILED_VALIDATION_IMAGE);
        when(mockImageValidator.validate(any(Mono.class), eq(duration))).thenReturn(Mono.just(validationResponse));

        Mono<ApiResponse> responseMono = imageProcessor.processNewImage(request);

        StepVerifier.create(responseMono)
                .expectNextMatches(response ->
                        response.getCode() == StatusCodes.FAILED_VALIDATION_IMAGE.getCode()
                )
                .verifyComplete();
    }

    @Test
    void testProcessNewImage_databaseSaveFailure() {
        String imageUrl = "http://example.com/image.png";
        int duration = 3000;
        AddImageRequest request = new AddImageRequest(imageUrl, duration);

        var mockClientResponse = mock(ClientResponse.class);
        when(mockFetcher.fetchRequest(any(RequestBuilder.class))).thenReturn(Mono.just(mockClientResponse));

        ApiResponse validationResponse = ApiResponse.success(StatusCodes.OK, Collections.emptyList());
        when(mockImageValidator.validate(any(Mono.class), eq(duration))).thenReturn(Mono.just(validationResponse));

        when(mockUrlUtils.extractImageType(validationResponse)).thenReturn(java.util.Optional.of("image/png"));
        when(mockUrlUtils.extractKeywordsFromUrl(imageUrl)).thenReturn(List.of("keyword1", "keyword2"));

        when(mockDatabaseAPI.saveNewImage(any(Image.class), anyList())).thenReturn(Mono.error(new RuntimeException("Database error")));

        Mono<ApiResponse> responseMono = imageProcessor.processNewImage(request);

        StepVerifier.create(responseMono)
                .expectNextMatches(response ->
                        response.getCode() == StatusCodes.DATABASE_OPERATION_FAILED.getCode()
                )
                .verifyComplete();
    }

    @Test
    void testProcessNewImage_missingImageType() {
        String imageUrl = "http://example.com/image.png";
        int duration = 3000;
        AddImageRequest request = new AddImageRequest(imageUrl, duration);

        var mockClientResponse = mock(ClientResponse.class);
        when(mockFetcher.fetchRequest(any(RequestBuilder.class))).thenReturn(Mono.just(mockClientResponse));

        ApiResponse validationResponse = ApiResponse.success(StatusCodes.OK, Collections.emptyList());
        when(mockImageValidator.validate(any(Mono.class), eq(duration))).thenReturn(Mono.just(validationResponse));

        when(mockUrlUtils.extractImageType(validationResponse)).thenReturn(java.util.Optional.empty());

        Mono<ApiResponse> responseMono = imageProcessor.processNewImage(request);

        StepVerifier.create(responseMono)
                .expectNextMatches(response ->
                        response.getCode() == StatusCodes.FAILED_VALIDATION_IMAGE.getCode()
                )
                .verifyComplete();
    }
}
