package com.novisign.slideshow.task.slideshow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableR2dbcRepositories
@EnableTransactionManagement
public class TaskSlideshowApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskSlideshowApplication.class, args);
    }

}
