package com.novisign.slideshow.task.slideshow.utils;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class ConverterUtilsTest {
    private final ConverterUtils converterUtils = new ConverterUtils();

    @Test
    void parseToLong_validId_returnsMonoWithLong() {
        String validId = "12345";

        Mono<Long> result = converterUtils.parseToLong(validId);

        StepVerifier.create(result)
                .expectNext(12345L)
                .verifyComplete();
    }

    @Test
    void parseToLong_invalidId_returnsMonoWithError() {
        String invalidId = "abc";

        Mono<Long> result = converterUtils.parseToLong(invalidId);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("Invalid ID format: " + invalidId))
                .verify();
    }

    @Test
    void parseToLong_nullId_returnsMonoWithError() {
        String nullId = null;

        Mono<Long> result = converterUtils.parseToLong(nullId);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("Invalid ID format: null"))
                .verify();
    }

}