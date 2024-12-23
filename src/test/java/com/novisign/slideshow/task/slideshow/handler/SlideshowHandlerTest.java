package com.novisign.slideshow.task.slideshow.handler;

import com.novisign.slideshow.task.slideshow.constant.StatusCodes;
import com.novisign.slideshow.task.slideshow.entity.ProofOfPlay;
import com.novisign.slideshow.task.slideshow.model.AddSlideshowRequest;
import com.novisign.slideshow.task.slideshow.model.ApiResponse;
import com.novisign.slideshow.task.slideshow.service.SlideshowService;
import com.novisign.slideshow.task.slideshow.utils.ConverterUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class SlideshowHandlerTest {


    @Mock
    private SlideshowService slideshowService;

    @Mock
    private ConverterUtils converterUtils;

    @Mock
    private ServerRequest serverRequest;

    private SlideshowHandler slideshowHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        slideshowHandler = new SlideshowHandler(slideshowService, converterUtils);
    }

    @Test
    void testAddSlideshow_Success() {
        AddSlideshowRequest request = new AddSlideshowRequest("test", Collections.emptyList());
        ApiResponse response = ApiResponse.success(StatusCodes.SUCCESS, null);

        when(serverRequest.bodyToMono(AddSlideshowRequest.class)).thenReturn(Mono.just(request));
        when(slideshowService.addSlideshow(request)).thenReturn(Mono.just(response));

        Mono<ServerResponse> result = slideshowHandler.addSlideshow(serverRequest);

        StepVerifier.create(result)
                .consumeNextWith(serverResponse -> {
                    assertEquals(HttpStatus.CREATED, serverResponse.statusCode());
                })
                .verifyComplete();

        verify(slideshowService, times(1)).addSlideshow(request);
    }

    @Test
    void testAddSlideshow_BadRequest() {
        when(serverRequest.bodyToMono(AddSlideshowRequest.class)).thenReturn(Mono.error(new RuntimeException("Invalid request")));

        Mono<ServerResponse> result = slideshowHandler.addSlideshow(serverRequest);

        StepVerifier.create(result)
                .consumeNextWith(serverResponse -> {
                    assertEquals(HttpStatus.BAD_REQUEST, serverResponse.statusCode());
                })
                .verifyComplete();
    }

    @Test
    void testDeleteSlideshow_Success() {
        String slideshowId = "123";
        ApiResponse response = ApiResponse.success(StatusCodes.NOT_FOUND, null);

        when(serverRequest.pathVariable("id")).thenReturn(slideshowId);
        when(slideshowService.deleteSlideshowById(Long.valueOf(slideshowId))).thenReturn(Mono.just(response));

        Mono<ServerResponse> result = slideshowHandler.deleteSlideshow(serverRequest);

        StepVerifier.create(result)
                .consumeNextWith(serverResponse -> {
                    assertEquals(HttpStatus.BAD_REQUEST, serverResponse.statusCode());
                })
                .verifyComplete();

        verify(slideshowService, times(1)).deleteSlideshowById(Long.valueOf(slideshowId));
    }

    @Test
    void testSlideshowOrder_Success() {
        String slideshowId = "123";
        when(converterUtils.parseToLong(slideshowId)).thenReturn(Mono.just(123L));

        ApiResponse response = ApiResponse.success(StatusCodes.OK, null);
        when(slideshowService.slideshowOrder(123L)).thenReturn(Flux.just(response));

        when(serverRequest.pathVariable("id")).thenReturn(slideshowId);

        Mono<ServerResponse> result = slideshowHandler.slideshowOrder(serverRequest);

        StepVerifier.create(result)
                .consumeNextWith(serverResponse -> {
                    assertEquals(HttpStatus.OK, serverResponse.statusCode());
                })
                .verifyComplete();

        verify(slideshowService, times(1));
    }

    @Test
    void testProofOfPlay_Success() {
        String slideshowId = "123";
        String imageId = "456";
        when(converterUtils.parseToLong(slideshowId)).thenReturn(Mono.just(123L));
        when(converterUtils.parseToLong(imageId)).thenReturn(Mono.just(456L));

        ProofOfPlay proofOfPlay = new ProofOfPlay(123L, 456L);
        ApiResponse response = ApiResponse.success(StatusCodes.SUCCESS, null);
        when(slideshowService.saveProofOfPlay(proofOfPlay)).thenReturn(Mono.just(response));

        when(serverRequest.pathVariable("id")).thenReturn(slideshowId);
        when(serverRequest.pathVariable("imageId")).thenReturn(imageId);

        Mono<ServerResponse> result = slideshowHandler.proofOfPlay(serverRequest);

        StepVerifier.create(result)
                .consumeNextWith(serverResponse -> {
                    assertEquals(HttpStatus.BAD_REQUEST, serverResponse.statusCode());
                })
                .verifyComplete();

        verify(slideshowService, times(1));
    }

    @Test
    void testSlideshowOrder_BadRequest() {
        when(converterUtils.parseToLong("invalid")).thenReturn(Mono.error(new RuntimeException("Invalid request")));

        when(serverRequest.pathVariable("id")).thenReturn("invalid");

        Mono<ServerResponse> result = slideshowHandler.slideshowOrder(serverRequest);

        StepVerifier.create(result)
                .consumeNextWith(serverResponse -> {
                    assertEquals(HttpStatus.OK, serverResponse.statusCode());
                })
                .verifyComplete();
    }
}