package com.novisign.slideshow.task.slideshow.utils;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ConverterUtils {

    public Mono<Long> parseToLong(String id) {
        try {
            return Mono.just(Long.valueOf(id));
        } catch (NumberFormatException e) {
            return Mono.error(new IllegalArgumentException("Invalid ID format: " + id));
        }
    }

}
