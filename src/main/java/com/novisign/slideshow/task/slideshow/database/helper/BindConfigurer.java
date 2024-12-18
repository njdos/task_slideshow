package com.novisign.slideshow.task.slideshow.database.helper;

import org.springframework.r2dbc.core.DatabaseClient;

@FunctionalInterface
public interface BindConfigurer {
    DatabaseClient.GenericExecuteSpec configure(DatabaseClient.GenericExecuteSpec bindSpec);
}
