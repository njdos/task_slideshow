package com.novisign.slideshow.task.slideshow.database.queryMapping;

import com.novisign.slideshow.task.slideshow.mapper.Mapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.util.function.BiFunction;

public enum SlideshowImageQueryMapping implements QueryMapping {

    CREATE_ENTITY("""
            INSERT INTO slideshow_image (slideshow_id, image_id, duration)
            VALUES (:slideshow_id, :image_id, :duration)
            RETURNING id
            """,
            Mapper.mapRowToId
    ),
    GET_ENTITY_BY_IMAGES_ID("""
            SELECT * FROM slideshow_image
            WHERE image_id IN (:image_ids)
            """,
            Mapper.mapRowToSlideshowImage
    ),
    GET_PK_BY_IMAGE_ID("""
            SELECT id FROM slideshow_image
            WHERE image_id = :image_id
            """,
            Mapper.mapRowToId
    ),
    GET_PK_BY_SLIDESHOW_ID("""
            SELECT id FROM slideshow_image
            WHERE slideshow_id = :slideshow_id
            """,
            Mapper.mapRowToId
    ),
    GET_ENTITY_BY_SLIDESHOW_ID("""
            SELECT * FROM slideshow_image
            WHERE slideshow_id = :slideshow_id
             """,
            Mapper.mapRowToSlideshowImage
    ),
    DELETE_ENTITY(
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
