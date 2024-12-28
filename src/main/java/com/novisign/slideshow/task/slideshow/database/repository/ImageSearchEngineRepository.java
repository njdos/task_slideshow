package com.novisign.slideshow.task.slideshow.database.repository;

import com.novisign.slideshow.task.slideshow.database.helper.BindConfigurer;
import com.novisign.slideshow.task.slideshow.database.helper.DatabaseHelper;
import com.novisign.slideshow.task.slideshow.database.queryMapping.DynamicQueryMapping;
import com.novisign.slideshow.task.slideshow.database.queryMapping.ImageSearchEngineQueryMapping;
import com.novisign.slideshow.task.slideshow.entity.ImageSearchEngine;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@AllArgsConstructor
public class ImageSearchEngineRepository {

    private final DatabaseHelper databaseHelper;


    public Mono<Long> save(ImageSearchEngine imageSearchEngine) {
        return databaseHelper.executeSaveOperation(
                ImageSearchEngineQueryMapping.CREATE_ENTITY,
                spec -> spec.bind("value", imageSearchEngine.getValue())
                        .bind("type", imageSearchEngine.getType())
                        .bind("imageId", imageSearchEngine.getImageId()),
                "saving the image search engine"
        );
    }

    public Flux<Long> findIdsImageSearchByImageId(Long imageId) {
        return databaseHelper.executeForMany(
                ImageSearchEngineQueryMapping.GET_PK_BY_IMAGE_ID,
                spec -> spec.bind("image_id", imageId),
                "fetching image search engine by image id"
        );
    }

    public Mono<Boolean> deleteById(Long id) {
        return databaseHelper.executeDeleteOperation(
                ImageSearchEngineQueryMapping.DELETE_ENTITY,
                spec -> spec.bind("id", id),
                "deleting image search engine"
        );
    }

    public Flux<Long> search(DynamicQueryMapping dynamicQueryMapping, BindConfigurer bindConfigurer) {
        return databaseHelper.executeForMany(
                dynamicQueryMapping,
                bindConfigurer,
                "searching image by keyword and duration"
        );
    }
}
