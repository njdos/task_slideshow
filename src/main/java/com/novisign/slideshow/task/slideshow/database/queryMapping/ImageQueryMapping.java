package com.novisign.slideshow.task.slideshow.database.queryMapping;

import com.novisign.slideshow.task.slideshow.mapper.Mapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.util.function.BiFunction;

public enum ImageQueryMapping implements QueryMapping {

    GET_ENTITY_BY_URL(
            "SELECT * FROM image WHERE url = :url",
            Mapper.mapRowToImage
    ),
    GET_ENTITY_BY_IDs(
            "SELECT * FROM image WHERE id IN (:ids) ORDER BY added_time",
            Mapper.mapRowToImage
    ),
    GET_ENTITY_ID_AND_DURATION_BY_IDs(
            "SELECT id, duration FROM image WHERE id IN (:ids)",
            Mapper.mapRowToImageIdAndDuration
    ),
    CREATE_ENTITY("""
            INSERT INTO image (url, duration, type, added_time)
            VALUES (:url, :duration, :type, :addedTime)
            RETURNING id
            """,
            Mapper.mapRowToId
    ),
    DELETE_ENTITY(
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
