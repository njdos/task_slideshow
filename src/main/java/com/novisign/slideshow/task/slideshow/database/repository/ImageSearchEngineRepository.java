package com.novisign.slideshow.task.slideshow.database.repository;

import com.novisign.slideshow.task.slideshow.database.helper.DatabaseHelper;
import com.novisign.slideshow.task.slideshow.database.queryMapping.ImageSearchEngineQuery;
import com.novisign.slideshow.task.slideshow.entity.ImageSearchEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class ImageSearchEngineRepository {

    private final DatabaseHelper databaseHelper;

    @Autowired
    public ImageSearchEngineRepository(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }


    public Mono<Long> save(ImageSearchEngine imageSearchEngine) {
        return databaseHelper.executeSaveOperation(
                ImageSearchEngineQuery.CREATE_IMAGE_SEARCH,
                spec -> spec.bind("value", imageSearchEngine.getValue())
                        .bind("type", imageSearchEngine.getType())
                        .bind("imageId", imageSearchEngine.getImageId()),
                "saving the image search engine"
        );
    }

    public Flux<Long> findIdsImageSearchByImageId(Long imageId) {
        return databaseHelper.executeForMany(
                ImageSearchEngineQuery.GET_IMAGE_SEARCH_BY_IMAGE_ID,
                spec -> spec.bind("image_id", imageId),
                "fetching image search engine by image id"
        );
    }

    public Mono<Boolean> deleteById(Long id) {
        return databaseHelper.executeDeleteOperation(
                ImageSearchEngineQuery.DELETE_IMAGE_SEARCH_BY_ID,
                spec -> spec.bind("id", id),
                "deleting image search engine"
        );
    }

}
