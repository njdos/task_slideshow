package com.novisign.slideshow.task.slideshow.database.repository;

import com.novisign.slideshow.task.slideshow.database.query.ImageSearchEngineQuery;
import com.novisign.slideshow.task.slideshow.entity.ImageSearchEngine;
import com.novisign.slideshow.task.slideshow.exception.CustomDatabaseException;
import com.novisign.slideshow.task.slideshow.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
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

    public Flux<Long> findIdsImageSearchByImageId(Long imageId) {
        return databaseClient.sql(ImageSearchEngineQuery.GET_IMAGE_SEARCH_BY_IMAGE_ID.getQuery())
                .bind("image_id", imageId)
                .map(Mapper.mapRowToId)
                .all()
                .doOnError(e -> System.err.println("Error while querying the database: " + e.getMessage()))
                .onErrorResume(e -> Flux.error(new CustomDatabaseException("Error while querying the database", e)));
    }

    public Flux<Long> deleteById(Long id) {
        return databaseClient.sql(ImageSearchEngineQuery.DELETE_IMAGE_SEARCH_BY_ID.getQuery())
                .bind("id", id)
                .map(Mapper.mapRowToId)
                .all()
                .onErrorResume(e -> Flux.error(new CustomDatabaseException("Error while querying the database", e)))
                .switchIfEmpty(Flux.defer(() -> Flux.empty()));
    }

}
