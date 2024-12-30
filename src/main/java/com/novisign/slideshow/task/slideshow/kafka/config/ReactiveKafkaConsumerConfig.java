package com.novisign.slideshow.task.slideshow.kafka.config;

import com.novisign.slideshow.task.slideshow.kafka.constants.KafkaConstants;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import reactor.kafka.receiver.ReceiverOptions;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@AllArgsConstructor
public class ReactiveKafkaConsumerConfig {

    private KafkaConstants kafkaConstants;

    @Bean
    public ReactiveKafkaConsumerTemplate<Object, Object> imageEventConsumerTemplate() {
        return new ReactiveKafkaConsumerTemplate<>(receiverOptions(
                Collections.singletonList(kafkaConstants.getIMAGE_TOPIC_NAME()))
        );
    }

    @Bean
    public ReactiveKafkaConsumerTemplate<Object, Object> slideshowEventConsumerTemplate() {
        return new ReactiveKafkaConsumerTemplate<>(receiverOptions(
                Collections.singletonList(kafkaConstants.getSLIDESHOW_TOPIC_NAME()))
        );
    }

    private ReceiverOptions<Object, Object> receiverOptions(List<String> topics) {
        Map<String, Object> consumerProps = new HashMap<>();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConstants.getBOOTSTRAP_SERVERS());
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConstants.getGROUP_ID());
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        consumerProps.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumerProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        return ReceiverOptions.create(consumerProps)
                .subscription(topics);
    }

}
