package com.novisign.slideshow.task.slideshow.database.queryMapping;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.util.function.BiFunction;

public class DynamicQueryMapping implements QueryMapping {

    private final String query;
    private final BiFunction<Row, RowMetadata, ?> mapping;

    public DynamicQueryMapping(String query, BiFunction<Row, RowMetadata, ?> mapping) {
        this.query = query;
        this.mapping = mapping;
    }

    @Override
    public String getQuery() {
        return query;
    }

    @Override
    public <T> BiFunction<Row, RowMetadata, ?> getMapping() {
        return mapping;
    }

}
