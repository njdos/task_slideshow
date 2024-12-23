package com.novisign.slideshow.task.slideshow.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GeneralHandlerTest {

    @Mock
    private ServerRequest serverRequest;

    private GeneralHandler generalHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        generalHandler = new GeneralHandler();
    }

    @Test
    void testStatusCodes_Success() {
        Map<Integer, String> expectedStatusCodes = Map.of(
                200, "OK",
                201, "Created",
                400, "Bad Request",
                404, "Not Found",
                500, "Internal Server Error"
        );

        Mono<ServerResponse> result = generalHandler.statusCodes(serverRequest);

        StepVerifier.create(result)
                .consumeNextWith(serverResponse -> {
                    assertEquals(HttpStatus.OK, serverResponse.statusCode());

                })
                .verifyComplete();
    }
    @Test
    void testFavicon_Success() {
        Mono<ServerResponse> result = generalHandler.favicon(serverRequest);

        StepVerifier.create(result)
                .consumeNextWith(serverResponse -> {
                    assertEquals(HttpStatus.OK, serverResponse.statusCode());

                    assertEquals(HttpStatus.OK, serverResponse.statusCode());
                })
                .verifyComplete();
    }
}