package com.novisign.slideshow.task.slideshow.kafka.config;

import com.novisign.slideshow.task.slideshow.kafka.constants.KafkaConstants;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@AllArgsConstructor
public class KafkaTopicConfig {

    private KafkaConstants kafkaConstants;

    @Bean
    public NewTopic imageTopic() {
        return createTopic(kafkaConstants.getIMAGE_TOPIC_NAME(), 1, kafkaConstants.getREPLICA_COUNT());
    }

    @Bean
    public NewTopic slideshowTopic() {
        return createTopic(kafkaConstants.getSLIDESHOW_TOPIC_NAME(), 1, kafkaConstants.getREPLICA_COUNT());
    }

    private NewTopic createTopic(String topicName, int partitions, int replicas) {
        return TopicBuilder.name(topicName)
                .partitions(partitions)
                .replicas(replicas)
                .config("cleanup.policy", "delete")
                .config("retention.ms", String.valueOf(168 * 60 * 60 * 1000))
                .config("min.insync.replicas", String.valueOf(kafkaConstants.getREPLICA_COUNT() - 1))
                .build();
    }
}