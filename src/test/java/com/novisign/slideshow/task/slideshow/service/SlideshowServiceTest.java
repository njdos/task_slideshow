package com.novisign.slideshow.task.slideshow.service;

import com.novisign.slideshow.task.slideshow.constant.StatusCodes;
import com.novisign.slideshow.task.slideshow.database.DatabaseAPI;
import com.novisign.slideshow.task.slideshow.entity.ProofOfPlay;
import com.novisign.slideshow.task.slideshow.kafka.KafkaAPI;
import com.novisign.slideshow.task.slideshow.model.AddSlideshowRequest;
import com.novisign.slideshow.task.slideshow.model.ApiResponse;
import com.novisign.slideshow.task.slideshow.processor.SlideShowProcessor;
import com.novisign.slideshow.task.slideshow.validator.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

class SlideshowServiceTest {

    @Mock
    private DatabaseAPI databaseAPI;

    @Mock
    private SlideShowProcessor slideShowProcessor;

    @Mock
    private Validator validator;

    @Mock
    private KafkaAPI kafkaAPI;

    private SlideshowService slideshowService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        slideshowService = new SlideshowService(kafkaAPI, databaseAPI, slideShowProcessor, validator);
    }

    @Test
    void testAddSlideshow_invalidRequest() {
        AddSlideshowRequest request = new AddSlideshowRequest("Test Slideshow", List.of());

        when(slideShowProcessor.isValidRequest(request)).thenReturn(Mono.just(ApiResponse.error(StatusCodes.INVALID_REQUEST_BODY)));

        Mono<ApiResponse> result = slideshowService.addSlideshow(request);

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getCode() == StatusCodes.INVALID_REQUEST_BODY.getCode())
                .verifyComplete();

        verify(slideShowProcessor, times(1)).isValidRequest(request);
    }

    @Test
    void testDeleteSlideshowById_notFound() {
        Long slideshowId = 1L;

        when(databaseAPI.deleteSlideshowById(slideshowId)).thenReturn(Mono.just(false));

        Mono<ApiResponse> result = slideshowService.deleteSlideshowById(slideshowId);

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getCode() == StatusCodes.NOT_FOUND.getCode())
                .verifyComplete();

        verify(databaseAPI, times(1)).deleteSlideshowById(slideshowId);
    }

    @Test
    void testSlideshowOrder_success() {
        Long slideshowId = 1L;

        when(databaseAPI.slideshowOrder(slideshowId)).thenReturn(Flux.just(ApiResponse.success(StatusCodes.SUCCESS, Collections.emptyList())));

        Flux<ApiResponse> result = slideshowService.slideshowOrder(slideshowId);

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getCode() == StatusCodes.SUCCESS.getCode())
                .verifyComplete();

        verify(databaseAPI, times(1)).slideshowOrder(slideshowId);
    }

    @Test
    void testSaveProofOfPlay_success() {
        ProofOfPlay proofOfPlay = new ProofOfPlay(1L, 2L);

        when(databaseAPI.saveProofOfPlay(proofOfPlay)).thenReturn(Mono.just(1L));

        Mono<ApiResponse> result = slideshowService.saveProofOfPlay(proofOfPlay);

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getCode() == StatusCodes.OK.getCode())
                .verifyComplete();

        verify(databaseAPI, times(1)).saveProofOfPlay(proofOfPlay);
    }

    @Test
    void testSaveProofOfPlay_failure() {
        ProofOfPlay proofOfPlay = new ProofOfPlay(1L, 2L);

        when(databaseAPI.saveProofOfPlay(proofOfPlay)).thenReturn(Mono.just(0L));

        Mono<ApiResponse> result = slideshowService.saveProofOfPlay(proofOfPlay);

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getCode() == StatusCodes.DATABASE_OPERATION_FAILED.getCode())
                .verifyComplete();

        verify(databaseAPI, times(1)).saveProofOfPlay(proofOfPlay);
    }

    @Test
    void testSaveProofOfPlay_error() {
        ProofOfPlay proofOfPlay = new ProofOfPlay(1L, 2L);

        when(databaseAPI.saveProofOfPlay(proofOfPlay)).thenReturn(Mono.error(new RuntimeException("Database error")));

        Mono<ApiResponse> result = slideshowService.saveProofOfPlay(proofOfPlay);

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getCode() == StatusCodes.DATABASE_OPERATION_FAILED.getCode())
                .verifyComplete();

        verify(databaseAPI, times(1)).saveProofOfPlay(proofOfPlay);
    }

    @Test
    void testProcessSlideshowWithDelays() {
        List<ApiResponse> responses = List.of(
                ApiResponse.success(StatusCodes.OK, List.of(Collections.singletonMap("duration", 3))),
                ApiResponse.success(StatusCodes.OK, List.of(Collections.singletonMap("duration", 5)))
        );

        when(databaseAPI.slideshowOrder(anyLong()))
                .thenReturn(Flux.just(ApiResponse.success(StatusCodes.SUCCESS, Collections.emptyList())));

        Flux<ApiResponse> result = slideshowService.processSlideshowWithDelays(responses);

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getCode() == StatusCodes.OK.getCode())
                .expectNextMatches(response -> response.getCode() == StatusCodes.OK.getCode())
                .expectComplete()
                .verifyLater();
    }

}