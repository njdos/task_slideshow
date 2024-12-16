package com.novisign.slideshow.task.slideshow.validator;

import com.novisign.slideshow.task.slideshow.constant.StatusCodes;
import com.novisign.slideshow.task.slideshow.model.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ImageValidatorTest {

    private ImageValidator imageValidator;

    @Mock
    private ClientResponse mockClientResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        imageValidator = new ImageValidator();
    }

    @Test
    void testValidate_validImageAndDuration() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "image/png");

        ClientResponse.Headers mockHeaders = mock(ClientResponse.Headers.class);
        when(mockHeaders.asHttpHeaders()).thenReturn(headers);
        when(mockClientResponse.headers()).thenReturn(mockHeaders);

        var responseMono = (Mono<ApiResponse>) invokePrivateMethod(imageValidator,
                "validateImage",
                new Class<?>[]{Mono.class},
                new Object[]{Mono.just(mockClientResponse)});

        StepVerifier.create(responseMono)
                .expectNextMatches(response ->
                        response.getCode() == StatusCodes.OK.getCode() &&
                                response.getData().contains(Map.of("typeImage", "image/png"))
                )
                .verifyComplete();
    }

    @Test
    void testValidate_validImageAndInvalidDuration() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "image/png");

        ClientResponse.Headers mockHeaders = mock(ClientResponse.Headers.class);
        when(mockHeaders.asHttpHeaders()).thenReturn(headers);
        when(mockClientResponse.headers()).thenReturn(mockHeaders);

        var responseMono = (Mono<ApiResponse>) invokePrivateMethod(imageValidator,
                "validate",
                new Class<?>[]{Mono.class, int.class},
                new Object[]{Mono.just(mockClientResponse), 5000});

        StepVerifier.create(responseMono)
                .expectNextMatches(response ->
                        response.getCode() == StatusCodes.FAILED_VALIDATION_DURATION.getCode()
                )
                .verifyComplete();
    }

    @Test
    void testValidate_invalidImageType() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "text/plain");

        ClientResponse.Headers mockHeaders = mock(ClientResponse.Headers.class);
        when(mockHeaders.asHttpHeaders()).thenReturn(headers);
        when(mockClientResponse.headers()).thenReturn(mockHeaders);

        var responseMono = (Mono<ApiResponse>) invokePrivateMethod(imageValidator,
                "validateImage",
                new Class<?>[]{Mono.class},
                new Object[]{Mono.just(mockClientResponse)});

        StepVerifier.create(responseMono)
                .expectNextMatches(response ->
                        response.getCode() == StatusCodes.FAILED_VALIDATION_IMAGE.getCode()
                )
                .verifyComplete();
    }

    @Test
    void testValidate_errorDuringValidation() throws Exception {
        when(mockClientResponse.headers()).thenThrow(new RuntimeException("Processing error"));

        var responseMono = (Mono<ApiResponse>) invokePrivateMethod(imageValidator,
                "validate",
                new Class<?>[]{Mono.class, int.class},
                new Object[]{Mono.just(mockClientResponse), 500});

        StepVerifier.create(responseMono)
                .expectNextMatches(response ->
                        response.getCode() == StatusCodes.FAILED_VALIDATION.getCode()
                )
                .verifyComplete();
    }

    @Test
    void testValidateImage_validContentType() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "image/png");

        ClientResponse.Headers mockHeaders = mock(ClientResponse.Headers.class);
        when(mockHeaders.asHttpHeaders()).thenReturn(headers);
        when(mockClientResponse.headers()).thenReturn(mockHeaders);

        Mono<ApiResponse> responseMono = (Mono<ApiResponse>) invokePrivateMethod(imageValidator,
                "validateImage",
                new Class<?>[]{Mono.class},
                new Object[]{Mono.just(mockClientResponse)});

        StepVerifier.create(responseMono)
                .expectNextMatches(response ->
                        response.getCode() == StatusCodes.OK.getCode() &&
                                response.getData().contains(Map.of("typeImage", "image/png"))
                )
                .verifyComplete();
    }

    @Test
    void testValidateImage_invalidContentType() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "text/plain");

        ClientResponse.Headers mockHeaders = mock(ClientResponse.Headers.class);
        when(mockHeaders.asHttpHeaders()).thenReturn(headers);
        when(mockClientResponse.headers()).thenReturn(mockHeaders);

        Mono<ApiResponse> responseMono = (Mono<ApiResponse>) invokePrivateMethod(imageValidator,
                "validateImage",
                new Class<?>[]{Mono.class},
                new Object[]{Mono.just(mockClientResponse)});

        StepVerifier.create(responseMono)
                .expectNextMatches(response ->
                        response.getCode() == StatusCodes.FAILED_VALIDATION_IMAGE.getCode()
                )
                .verifyComplete();
    }

    @Test
    void testValidateImage_errorProcessing() throws Exception {
        when(mockClientResponse.headers()).thenThrow(new RuntimeException("Processing error"));

        Mono<ApiResponse> responseMono = (Mono<ApiResponse>) invokePrivateMethod(imageValidator,
                "validateImage",
                new Class<?>[]{Mono.class},
                new Object[]{Mono.just(mockClientResponse)});

        StepVerifier.create(responseMono)
                .expectNextMatches(response ->
                        response.getCode() == StatusCodes.FAILED_VALIDATION.getCode()
                )
                .verifyComplete();
    }

    @Test
    void testProcessClientResponse_validContentType() throws Exception {
        ClientResponse.Headers mockHeaders = mock(ClientResponse.Headers.class);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "image/png");

        when(mockHeaders.asHttpHeaders()).thenReturn(headers);
        when(mockClientResponse.headers()).thenReturn(mockHeaders);

        var responseMono = (Mono<ApiResponse>) invokePrivateMethod(imageValidator,
                "processClientResponse",
                new Class<?>[]{ClientResponse.class},
                new Object[]{mockClientResponse});

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.getCode() == StatusCodes.OK.getCode())
                .verifyComplete();
    }

    @Test
    void testProcessClientResponse_invalidContentType() throws Exception {
        ClientResponse.Headers mockHeaders = mock(ClientResponse.Headers.class);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "text/plain");

        when(mockHeaders.asHttpHeaders()).thenReturn(headers);
        when(mockClientResponse.headers()).thenReturn(mockHeaders);

        var responseMono = (Mono<ApiResponse>) invokePrivateMethod(imageValidator,
                "processClientResponse",
                new Class<?>[]{ClientResponse.class},
                new Object[]{mockClientResponse});

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.getCode() == StatusCodes.FAILED_VALIDATION_IMAGE.getCode())
                .verifyComplete();
    }

    @Test
    void testProcessClientResponse_emptyContentType() throws Exception {
        ClientResponse.Headers mockHeaders = mock(ClientResponse.Headers.class);

        HttpHeaders headers = new HttpHeaders();

        when(mockHeaders.asHttpHeaders()).thenReturn(headers);
        when(mockClientResponse.headers()).thenReturn(mockHeaders);

        var responseMono = (Mono<ApiResponse>) invokePrivateMethod(imageValidator,
                "processClientResponse",
                new Class<?>[]{ClientResponse.class},
                new Object[]{mockClientResponse});


        StepVerifier.create(responseMono)
                .expectNextMatches(response ->
                        response.getCode() == StatusCodes.FAILED_VALIDATION_IMAGE.getCode()
                )
                .verifyComplete();
    }

    @Test
    void testValidateContentType_validImageType() throws Exception {
        var type = "image/png";

        var resultMono = (Mono<Optional<String>>) invokePrivateMethod(imageValidator,
                "validateContentType",
                new Class<?>[]{List.class},
                new Object[]{List.of(type)});

        StepVerifier.create(resultMono)
                .expectNextMatches(optional -> optional.isPresent() && type.equals(optional.get()))
                .verifyComplete();
    }

    @Test
    void testValidateContentType_invalidImageType() throws Exception {
        var resultMono = (Mono<Optional<String>>) invokePrivateMethod(imageValidator,
                "validateContentType",
                new Class<?>[]{List.class},
                new Object[]{List.of("text/plain")});

        StepVerifier.create(resultMono)
                .expectNextMatches(optional -> optional.isEmpty())
                .verifyComplete();
    }

    @Test
    void testValidateDuration_validDuration() throws Exception {
        ApiResponse validationResponse = ApiResponse.success(StatusCodes.OK, List.of());

        var resultMono = (Mono<ApiResponse>) invokePrivateMethod(imageValidator,
                "validateDuration",
                new Class<?>[]{int.class, ApiResponse.class},
                new Object[]{1000, validationResponse});

        StepVerifier.create(resultMono)
                .expectNextMatches(response -> response.getCode() == StatusCodes.OK.getCode())
                .verifyComplete();
    }

    @Test
    void testValidateDuration_invalidDuration() throws Exception {
        ApiResponse validationResponse = ApiResponse.success(StatusCodes.OK, List.of());

        var resultMono = (Mono<ApiResponse>) invokePrivateMethod(imageValidator,
                "validateDuration",
                new Class<?>[]{int.class, ApiResponse.class},
                new Object[]{4000, validationResponse});

        StepVerifier.create(resultMono)
                .expectNextMatches(response -> response.getCode() == StatusCodes.FAILED_VALIDATION_DURATION.getCode())
                .verifyComplete();
    }

    private Object invokePrivateMethod(Object obj, String methodName, Class<?>[] parameterTypes,
                                       Object[] parameters) throws Exception {
        Method method = obj.getClass().getDeclaredMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method.invoke(obj, parameters);
    }
}
