package com.novisign.slideshow.task.slideshow.exchange;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeoutException;

@Component
public class RetryPolicy {

    public Retry createRetryPolicy() {
        return Retry.backoff(3, Duration.ofSeconds(2))
                .filter(this::isRetryableError)
                .onRetryExhaustedThrow((spec, signal) ->
                        new RuntimeException("All retry attempts exhausted", signal.failure()));
    }

    private boolean isRetryableError(Throwable throwable) {
        if (throwable instanceof WebClientResponseException exception) {
            return exception.getStatusCode().is5xxServerError();
        }
        return throwable instanceof IOException || throwable instanceof TimeoutException;
    }

}
