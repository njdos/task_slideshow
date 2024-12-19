package com.novisign.slideshow.task.slideshow.database.queryMapping;

import com.novisign.slideshow.task.slideshow.constant.ImageSearchTypes;
import com.novisign.slideshow.task.slideshow.mapper.Mapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.util.function.BiFunction;

public enum ImageSearchEngineQuery implements QueryMapping {

    CREATE_IMAGE_SEARCH("""
            INSERT INTO image_search_engine (value, type, image_id)
            VALUES (:value, :type, :imageId)
            RETURNING id
            """,
            null
    ),
    GET_IMAGE_SEARCH_BY_IMAGE_ID(
            "SELECT id FROM image_search_engine WHERE image_id = :image_id",
            Mapper.mapRowToId
    ),
    FIND_IMAGES_BY_KEYWORD_OR_DURATION("""
                SELECT image_id as id
                FROM image_search_engine
                WHERE (type = '%s' AND value ILIKE :value1) OR (type = '%s' AND value = :value2)
            """.formatted(ImageSearchTypes.KEYWORD, ImageSearchTypes.DURATION),
            Mapper.mapRowToId
    ),
    DELETE_IMAGE_SEARCH_BY_ID(
            "DELETE FROM image_search_engine WHERE id = :id",
            null
    );

    private final String query;
    private final BiFunction<Row, RowMetadata, ?> mapping;

    ImageSearchEngineQuery(String query, BiFunction<Row, RowMetadata, ?> mapping) {
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
