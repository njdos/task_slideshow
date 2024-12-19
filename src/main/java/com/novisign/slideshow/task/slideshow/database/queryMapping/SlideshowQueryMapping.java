package com.novisign.slideshow.task.slideshow.database.queryMapping;

import com.novisign.slideshow.task.slideshow.mapper.Mapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.util.function.BiFunction;

public enum SlideshowQueryMapping implements QueryMapping {

    CREATE_ENTITY("""
            INSERT INTO slideshow (name, created_time)
            VALUES (:name, :created_time)
            RETURNING id
            """,
            Mapper.mapRowToId
    ),
    DELETE_ENTITY(
            "DELETE FROM slideshow WHERE id = :id",
            null
    );

    private final String query;
    private final BiFunction<Row, RowMetadata, ?> mapping;

    SlideshowQueryMapping(String query, BiFunction<Row, RowMetadata, ?> mapping) {
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
