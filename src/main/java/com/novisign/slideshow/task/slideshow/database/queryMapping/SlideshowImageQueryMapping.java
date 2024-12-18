package com.novisign.slideshow.task.slideshow.database.queryMapping;

import com.novisign.slideshow.task.slideshow.mapper.Mapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.util.function.BiFunction;

public enum SlideshowImageQueryMapping implements QueryMapping {

    CREATE_SLIDESHOW_IMAGE("""
            INSERT INTO slideshow_image (slideshow_id, image_id, duration)
            VALUES (:slideshow_id, :image_id, :duration)
            RETURNING id
            """,
            Mapper.mapRowToId
    ),
    GET_SLIDESHOW_IMAGE_BY_IMAGE_ID(
            "SELECT id FROM slideshow_image WHERE slideshow_id = :slideshow_id",
            Mapper.mapRowToId
    ),
    DELETE_SLIDESHOW_IMAGE_BY_ID(
            "DELETE FROM slideshow_image WHERE id = :id",
            null
    );

    private final String query;
    private final BiFunction<Row, RowMetadata, ?> mapping;

    SlideshowImageQueryMapping(String query, BiFunction<Row, RowMetadata, ?> mapping) {
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
