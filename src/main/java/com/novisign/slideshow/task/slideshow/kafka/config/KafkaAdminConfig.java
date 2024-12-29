package com.novisign.slideshow.task.slideshow.kafka.config;

import com.novisign.slideshow.task.slideshow.kafka.constants.KafkaConstants;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
@AllArgsConstructor
public class KafkaAdminConfig {

    private KafkaConstants kafkaConstants;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConstants.getBOOTSTRAP_SERVERS());
        return new KafkaAdmin(configs);
    }

}