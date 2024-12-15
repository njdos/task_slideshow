package com.novisign.slideshow.task.slideshow.mapper;

import com.novisign.slideshow.task.slideshow.entity.Image;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.util.function.BiFunction;

public class Mapper {

    public static final BiFunction<Row, RowMetadata, Image> mapRowToImage =
            (row, rowMetaData) -> {
                Image image = new Image();
                image.setId(row.get("id", Long.class));
                image.setUrl(row.get("url", String.class));
                image.setDuration(row.get("duration", Integer.class));
                image.setType(row.get("type", String.class));
                image.setAddedTime(row.get("added_time", java.time.LocalDateTime.class));
                return image;
            };

    public static final BiFunction<Row, RowMetadata, Long> mapRowToId =
            (row, rowMetaData) -> row.get("id", Long.class);

}
