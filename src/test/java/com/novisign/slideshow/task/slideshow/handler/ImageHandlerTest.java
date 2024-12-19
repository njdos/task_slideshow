package com.novisign.slideshow.task.slideshow.handler;

import com.novisign.slideshow.task.slideshow.constant.StatusCodes;
import com.novisign.slideshow.task.slideshow.model.AddImageRequest;
import com.novisign.slideshow.task.slideshow.model.ApiResponse;
import com.novisign.slideshow.task.slideshow.service.ImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ImageHandlerTest {

    @Mock
    private ImageService imageService;

    @Mock
    private ServerRequest serverRequest;

    private ImageHandler imageHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        imageHandler = new ImageHandler(imageService);
    }

    @Test
    void testAddImage_Success() {
        AddImageRequest request = new AddImageRequest("https://example.com/image.png", 30);
        ApiResponse response = ApiResponse.success(StatusCodes.SUCCESS, null);

        when(serverRequest.bodyToMono(AddImageRequest.class)).thenReturn(Mono.just(request));
        when(imageService.addImage(request)).thenReturn(Mono.just(response));

        Mono<ServerResponse> result = imageHandler.addImage(serverRequest);

        StepVerifier.create(result)
                .consumeNextWith(serverResponse -> {
                    assertEquals(HttpStatus.CREATED, serverResponse.statusCode());
                })
                .verifyComplete();

        verify(imageService, times(1)).addImage(request);
    }

    @Test
    void testAddImage_BadRequest_InvalidRequest() {
        when(serverRequest.bodyToMono(AddImageRequest.class)).thenReturn(Mono.error(new RuntimeException("Invalid request")));

        Mono<ServerResponse> result = imageHandler.addImage(serverRequest);

        StepVerifier.create(result)
                .consumeNextWith(serverResponse -> {
                    assertEquals(HttpStatus.BAD_REQUEST, serverResponse.statusCode());
                })
                .verifyComplete();
    }

    @Test
    void testAddImage_BadRequest_ServiceError() {
        AddImageRequest request = new AddImageRequest("https://example.com/image.png", 30);
        ApiResponse errorResponse = ApiResponse.error(StatusCodes.INVALID_REQUEST_BODY);

        when(serverRequest.bodyToMono(AddImageRequest.class)).thenReturn(Mono.just(request));
        when(imageService.addImage(request)).thenReturn(Mono.error(new RuntimeException("Service error")));

        Mono<ServerResponse> result = imageHandler.addImage(serverRequest);

        StepVerifier.create(result)
                .consumeNextWith(serverResponse -> {
                    assertEquals(HttpStatus.BAD_REQUEST, serverResponse.statusCode());
                })
                .verifyComplete();

        verify(imageService, times(1)).addImage(request);
    }

    @Test
    void testAddImage_FailedValidation() {
        AddImageRequest request = new AddImageRequest("https://example.com/image.png", 30);
        ApiResponse validationError = ApiResponse.error(StatusCodes.FAILED_VALIDATION);

        when(serverRequest.bodyToMono(AddImageRequest.class)).thenReturn(Mono.just(request));
        when(imageService.addImage(request)).thenReturn(Mono.just(validationError));

        Mono<ServerResponse> result = imageHandler.addImage(serverRequest);

        StepVerifier.create(result)
                .consumeNextWith(serverResponse -> {
                    assertEquals(HttpStatus.BAD_REQUEST, serverResponse.statusCode());
                })
                .verifyComplete();

        verify(imageService, times(1)).addImage(request);
    }
}
