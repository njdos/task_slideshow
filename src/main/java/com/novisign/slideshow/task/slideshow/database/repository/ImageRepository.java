package com.novisign.slideshow.task.slideshow.database.repository;

import com.novisign.slideshow.task.slideshow.database.query.ImageQuery;
import com.novisign.slideshow.task.slideshow.entity.Image;
import com.novisign.slideshow.task.slideshow.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class ImageRepository {

    private final DatabaseClient databaseClient;

    @Autowired
    public ImageRepository(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    public Mono<Image> findByUrl(String url) {
        return databaseClient.sql(ImageQuery.GET_IMAGE_BY_URL.getQuery())
                .bind("url", url)
                .map(Mapper.mapRowToImage)
                .one();
    }

    public Mono<Long> save(Image image) {
        return databaseClient.sql(ImageQuery.CREATE_IMAGE.getQuery())
                .bind("url", image.getUrl())
                .bind("duration", image.getDuration())
                .bind("type", image.getType())
                .bind("addedTime", image.getAddedTime())
                .map(Mapper.mapRowToId)
                .one();
    }
}
