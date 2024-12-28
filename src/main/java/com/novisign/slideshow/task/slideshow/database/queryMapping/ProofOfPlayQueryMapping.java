package com.novisign.slideshow.task.slideshow.database.queryMapping;

import com.novisign.slideshow.task.slideshow.mapper.Mapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.util.function.BiFunction;

public enum ProofOfPlayQueryMapping implements QueryMapping {

    CREATE_ENTITY("""
            INSERT INTO proof_of_play (slideshow_id, image_id, play_time)
            VALUES (:slideshow_id, :image_id, :play_time)
            RETURNING id
            """,
            Mapper.mapRowToId
    ),
    GET_PK_BY_IMAGE_ID(
            "SELECT id FROM proof_of_play WHERE image_id = :image_id",
            Mapper.mapRowToId
    ),
    GET_PK_BY_SLIDESHOW_ID(
            "SELECT id FROM proof_of_play WHERE slideshow_id = :slideshow_id",
            Mapper.mapRowToId
    ),
    DELETE_ENTITY(
            "DELETE FROM proof_of_play WHERE id = :id",
            null
    );;

    private final String query;
    private final BiFunction<Row, RowMetadata, ?> mapping;

    ProofOfPlayQueryMapping(String query, BiFunction<Row, RowMetadata, ?> mapping) {
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
