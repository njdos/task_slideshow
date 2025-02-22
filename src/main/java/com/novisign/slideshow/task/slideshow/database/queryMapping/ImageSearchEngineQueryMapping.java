package com.novisign.slideshow.task.slideshow.database.queryMapping;

import com.novisign.slideshow.task.slideshow.mapper.Mapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.util.function.BiFunction;

public enum ImageSearchEngineQueryMapping implements QueryMapping {

    CREATE_ENTITY("""
            INSERT INTO image_search_engine (value, type, image_id)
            VALUES (:value, :type, :imageId)
            RETURNING id
            """,
            Mapper.mapRowToId
    ),
    GET_PK_BY_IMAGE_ID(
            "SELECT id FROM image_search_engine WHERE image_id = :image_id",
            Mapper.mapRowToId
    ),
    DELETE_ENTITY(
            "DELETE FROM image_search_engine WHERE id = :id",
            null
    );

    private final String query;
    private final BiFunction<Row, RowMetadata, ?> mapping;


    ImageSearchEngineQueryMapping(String query, BiFunction<Row, RowMetadata, ?> mapping) {
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
