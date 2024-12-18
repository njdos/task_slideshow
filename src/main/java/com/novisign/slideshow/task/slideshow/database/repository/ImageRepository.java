package com.novisign.slideshow.task.slideshow.database.repository;

import com.novisign.slideshow.task.slideshow.database.helper.DatabaseHelper;
import com.novisign.slideshow.task.slideshow.database.queryMapping.ImageQueryMapping;
import com.novisign.slideshow.task.slideshow.entity.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public class ImageRepository {

    private final DatabaseHelper databaseHelper;

    @Autowired
    public ImageRepository(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }


    public Mono<Image> findImageByUrl(String url) {
        return databaseHelper.executeForOne(
                ImageQueryMapping.GET_IMAGE_BY_URL,
                spec -> spec.bind("url", url),
                "fetching the image"
        );
    }

    public Flux<Image> findImageIdAndDurationByIds(List<Long> ids) {
        return databaseHelper.executeForMany(
                ImageQueryMapping.GET_IMAGE_ID_AND_DURATION_BY_IDs,
                spec -> spec.bind("ids", ids),
                "fetching images by ids"
        );
    }

    public Mono<Long> save(Image image) {
        return databaseHelper.executeSaveOperation(
                ImageQueryMapping.CREATE_IMAGE,
                spec -> spec.bind("url", image.getUrl())
                        .bind("duration", image.getDuration())
                        .bind("type", image.getType())
                        .bind("addedTime", image.getAddedTime()),
                "saving the image"
        );
    }

    public Mono<Boolean> deleteById(Long id) {
        return databaseHelper.executeDeleteOperation(
                ImageQueryMapping.DELETE_IMAGE_BY_ID,
                spec -> spec.bind("id", id),
                "deleting the image"
        );
    }

}
