package com.novisign.slideshow.task.slideshow.utils;

import com.novisign.slideshow.task.slideshow.constant.StatusCodes;
import com.novisign.slideshow.task.slideshow.model.ApiResponse;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApiResponseUtilsTest {

    @Test
    public void testERROR_DATABASE_OPERATION_FAILED() {
        Throwable throwable = new RuntimeException("Database error");

        Mono<? extends ApiResponse> responseMono = ApiResponseUtils.ERROR_DATABASE_OPERATION_FAILED(throwable);

        StepVerifier.create(responseMono)
                .assertNext(apiResponse -> {
                    assertEquals(StatusCodes.DATABASE_OPERATION_FAILED.getCode(), apiResponse.getCode());
                    assertEquals("error", apiResponse.getStatus());
                    assertEquals(StatusCodes.DATABASE_OPERATION_FAILED.getMessage(), apiResponse.getMessage());
                    assertTrue(apiResponse.getData().isEmpty());
                })
                .expectComplete()
                .verify();
    }


}