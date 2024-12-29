package com.novisign.slideshow.task.slideshow.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.novisign.slideshow.task.slideshow.exception.JsonSerializationException;
import com.novisign.slideshow.task.slideshow.kafka.constants.KafkaConstants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
@Slf4j
public class KafkaProducerService {

    private final ReactiveKafkaProducerTemplate<String, String> reactiveKafkaProducerTemplate;
    private final KafkaConstants kafkaConstants;
    private final ObjectMapper objectMapper;

    public Mono<Void> publishImageEvent(String eventType, Object message) {
        return publishEvent(kafkaConstants.getIMAGE_TOPIC_NAME(), eventType, message);
    }

    public Mono<Void> publishSlideshowEvent(String eventType, Object message) {
        return publishEvent(kafkaConstants.getSLIDESHOW_TOPIC_NAME(), eventType, message);
    }

    private Mono<Void> publishEvent(String topicName, String eventType, Object message) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(message);

            return reactiveKafkaProducerTemplate.send(topicName, eventType, jsonMessage)
                    .doOnSuccess(result -> log.info("Event published: {} - Type: {} - Message: {}",
                            topicName, eventType, jsonMessage))
                    .doOnError(error -> {
                        throw new JsonSerializationException("Error while publishing event to Kafka", error);
                    })
                    .then();
        } catch (JsonProcessingException e) {
            throw new JsonSerializationException("JSON serialization failed", e);
        }
    }
}