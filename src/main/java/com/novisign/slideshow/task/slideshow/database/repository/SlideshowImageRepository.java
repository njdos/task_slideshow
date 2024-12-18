package com.novisign.slideshow.task.slideshow.database.repository;

import com.novisign.slideshow.task.slideshow.database.helper.DatabaseHelper;
import com.novisign.slideshow.task.slideshow.database.queryMapping.ImageSearchEngineQuery;
import com.novisign.slideshow.task.slideshow.database.queryMapping.SlideshowImageQueryMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class SlideshowImageRepository {

    private final DatabaseHelper databaseHelper;

    @Autowired
    public SlideshowImageRepository(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public Mono<Long> save(Long slideshowId, Long imageId, Integer duration) {
        return databaseHelper.executeSaveOperation(
                SlideshowImageQueryMapping.CREATE_SLIDESHOW_IMAGE,
                spec -> spec
                        .bind("slideshow_id", slideshowId)
                        .bind("image_id", imageId)
                        .bind("duration", duration),
                "saving slideshow image"
        );
    }

    public Flux<Long> findIdsSlideshowImagesBySlideshowId(Long slideshowId) {
        return databaseHelper.executeForMany(
                SlideshowImageQueryMapping.GET_SLIDESHOW_IMAGE_BY_IMAGE_ID,
                spec -> spec.bind("slideshow_id", slideshowId),
                "fetching slideshow image by slideshow id"
        );
    }

    public Mono<Boolean> deleteById(Long id) {
        return databaseHelper.executeDeleteOperation(
                SlideshowImageQueryMapping.DELETE_SLIDESHOW_IMAGE_BY_ID,
                spec -> spec.bind("id", id),
                "deleting slideshow image"
        );
    }

}
