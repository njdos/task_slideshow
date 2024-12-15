package com.novisign.slideshow.task.slideshow.database.repository;

import com.novisign.slideshow.task.slideshow.database.query.ImageSearchEngineQuery;
import com.novisign.slideshow.task.slideshow.entity.ImageSearchEngine;
import com.novisign.slideshow.task.slideshow.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class ImageSearchEngineRepository {

    private final DatabaseClient databaseClient;

    @Autowired
    public ImageSearchEngineRepository(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    public Mono<Long> save(ImageSearchEngine imageSearchEngine) {
        return databaseClient.sql(ImageSearchEngineQuery.CREATE_IMAGE_SEARCH.getQuery())
                .bind("value", imageSearchEngine.getValue())
                .bind("type", imageSearchEngine.getType())
                .bind("imageId", imageSearchEngine.getImageId())
                .map(Mapper.mapRowToId)
                .one()
                .onErrorReturn(-1L);
    }


}
