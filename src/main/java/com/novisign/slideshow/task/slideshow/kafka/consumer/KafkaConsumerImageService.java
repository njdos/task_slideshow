package com.novisign.slideshow.task.slideshow.kafka.consumer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.CommandLineRunner;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
@Slf4j
public class KafkaConsumerImageService implements CommandLineRunner {

    private final ReactiveKafkaConsumerTemplate<Object, Object> imageEventConsumerTemplate;

    private Flux<ConsumerRecord<Object, Object>> consumeImageEvents() {
        return imageEventConsumerTemplate
                .receiveAutoAck()
                .doOnNext(record -> log.info("Image Event - Key: {}, Value: {}, Offset: {}",
                        record.key(), record.value(), record.offset()))
                .doOnError(error -> log.error("Error while consuming image events: {}", error.getMessage()))
                .onErrorResume(error -> Mono.empty());
    }

    @Override
    public void run(String... args) {
        consumeImageEvents()
                .doOnSubscribe(subscription -> log.info("Consuming image events..."))
                .subscribe();
    }
}