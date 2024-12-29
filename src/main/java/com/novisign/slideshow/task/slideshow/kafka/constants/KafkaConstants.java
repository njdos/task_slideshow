package com.novisign.slideshow.task.slideshow.kafka.constants;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class KafkaConstants {

    @Value("${spring.kafka.bootstrap-servers}")
    private String BOOTSTRAP_SERVERS;

    @Value("${spring.kafka.consumer.group-id}")
    private String GROUP_ID;

    @Value("${image-actions.topic.name}")
    private String IMAGE_TOPIC_NAME;

    @Value("${slideshow-actions.topic.name}")
    private String SLIDESHOW_TOPIC_NAME;

}
