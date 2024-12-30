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
public class KafkaConsumerSlideshowService implements CommandLineRunner {

    private final ReactiveKafkaConsumerTemplate<Object, Object> slideshowEventConsumerTemplate;


    private Flux<Void> consumeSlideshowEvents() {
        return slideshowEventConsumerTemplate
                .receive()
                .flatMap(record -> processRecord(record)
                        .doOnSuccess(v -> record.receiverOffset().acknowledge())
                        .doOnError(error -> log.error("Error processing record: {}", error.getMessage())))
                .doOnError(error -> log.error("Error while consuming image events: {}", error.getMessage()))
                .onErrorResume(error -> Flux.empty());
    }

    private Mono<Void> processRecord(ConsumerRecord<Object, Object> record) {
        return Mono.fromRunnable(() -> {
            log.info("Slideshow Event - Key: {}, Value: {}, Offset: {}",
                    record.key(), record.value(), record.offset());
        });
    }

    @Override
    public void run(String... args) {
        consumeSlideshowEvents()
                .doOnSubscribe(subscription -> log.info("Consuming slideshow events..."))
                .subscribe();
    }
}