package com.novisign.slideshow.task.slideshow.database.queryMapping;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.util.function.BiFunction;

public interface QueryMapping {

    String getQuery();

    <T> BiFunction<Row, RowMetadata, ?> getMapping();

}
