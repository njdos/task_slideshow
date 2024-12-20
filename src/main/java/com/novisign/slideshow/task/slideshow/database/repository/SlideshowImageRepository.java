package com.novisign.slideshow.task.slideshow.database.repository;

import com.novisign.slideshow.task.slideshow.database.helper.DatabaseHelper;
import com.novisign.slideshow.task.slideshow.database.queryMapping.SlideshowImageQueryMapping;
import com.novisign.slideshow.task.slideshow.entity.SlideshowImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public class SlideshowImageRepository {

    private final DatabaseHelper databaseHelper;

    @Autowired
    public SlideshowImageRepository(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public Mono<Long> save(Long slideshowId, Long imageId, Integer duration) {
        return databaseHelper.executeSaveOperation(
                SlideshowImageQueryMapping.CREATE_ENTITY,
                spec -> spec
                        .bind("slideshow_id", slideshowId)
                        .bind("image_id", imageId)
                        .bind("duration", duration),
                "saving slideshow image"
        );
    }

    public Flux<Long> findIdsSlideshowImagesBySlideshowId(Long slideshowId) {
        return databaseHelper.executeForMany(
                SlideshowImageQueryMapping.GET_PK_BY_SLIDESHOW_ID,
                spec -> spec.bind("slideshow_id", slideshowId),
                "fetching pk slideshow image by slideshow id"
        );
    }

    public Flux<SlideshowImage> findIdsSlideshowImagesByImageIds(List<Long> imageIds) {
        return databaseHelper.executeForMany(
                SlideshowImageQueryMapping.GET_PK_BY_IMAGE_ID,
                spec -> spec.bind("image_ids", imageIds),
                "fetching slideshow image by slideshow ids"
        );
    }

    public Flux<SlideshowImage> findIdsSlideshowImagesBySlideshowIds(Long slideshowId) {
        return databaseHelper.executeForMany(
                SlideshowImageQueryMapping.GET_ENTITY_BY_SLIDESHOW_ID,
                spec -> spec.bind("slideshow_id", slideshowId),
                "fetching slideshow image by slideshow ids"
        );
    }

    public Mono<Boolean> deleteById(Long id) {
        return databaseHelper.executeDeleteOperation(
                SlideshowImageQueryMapping.DELETE_ENTITY,
                spec -> spec.bind("id", id),
                "deleting slideshow image"
        );
    }

}
