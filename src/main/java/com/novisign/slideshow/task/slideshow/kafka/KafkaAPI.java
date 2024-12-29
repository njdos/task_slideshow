package com.novisign.slideshow.task.slideshow.kafka;

import com.novisign.slideshow.task.slideshow.kafka.producer.KafkaProducerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class KafkaAPI {

    private final KafkaProducerService producerService;

    public Mono<Void> publishSlideshowEvent(String eventType, String eventMessage) {
        return producerService.publishSlideshowEvent(eventType, eventMessage);
    }

    public Mono<Void> publishImageEvent(String eventType, String eventMessage) {
        return producerService.publishImageEvent(eventType, eventMessage);
    }
}
