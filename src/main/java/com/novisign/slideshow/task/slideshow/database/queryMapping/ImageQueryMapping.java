package com.novisign.slideshow.task.slideshow.database.queryMapping;

import com.novisign.slideshow.task.slideshow.mapper.Mapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.util.function.BiFunction;

public enum ImageQueryMapping implements QueryMapping {

    GET_IMAGE_BY_URL(
            "SELECT * FROM image WHERE url = :url",
            Mapper.mapRowToImage
    ),
    CREATE_IMAGE("""
            INSERT INTO image (url, duration, type, added_time)
            VALUES (:url, :duration, :type, :addedTime)
            RETURNING id
            """,
            Mapper.mapRowToId
    ),
    DELETE_IMAGE_BY_ID(
            "DELETE FROM image WHERE id = :id",
            null
    );

    private final String query;
    private final BiFunction<Row, RowMetadata, ?> mapping;

    ImageQueryMapping(String query, BiFunction<Row, RowMetadata, ?> mapping) {
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
