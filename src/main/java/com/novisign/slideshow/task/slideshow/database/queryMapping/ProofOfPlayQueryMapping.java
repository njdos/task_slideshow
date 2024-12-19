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
    );

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