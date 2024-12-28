package com.novisign.slideshow.task.slideshow.database.repository;

import com.novisign.slideshow.task.slideshow.database.helper.DatabaseHelper;
import com.novisign.slideshow.task.slideshow.database.queryMapping.ImageQueryMapping;
import com.novisign.slideshow.task.slideshow.entity.Image;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
@AllArgsConstructor
public class ImageRepository {

    private final DatabaseHelper databaseHelper;

    public Mono<Image> findImageByUrl(String url) {
        return databaseHelper.executeForOne(
                ImageQueryMapping.GET_ENTITY_BY_URL,
                spec -> spec.bind("url", url),
                "fetching the image"
        );
    }

    public Flux<Image> findImageIdAndDurationByIds(List<Long> ids) {
        return databaseHelper.executeForMany(
                ImageQueryMapping.GET_ENTITY_ID_AND_DURATION_BY_IDs,
                spec -> spec.bind("ids", ids),
                "fetching image id and duration by ids"
        );
    }

    public Flux<Image> findImageByIds(List<Long> ids) {
        return databaseHelper.executeForMany(
                ImageQueryMapping.GET_ENTITY_BY_IDs,
                spec -> spec.bind("ids", ids),
                "fetching images by ids"
        );
    }

    public Mono<Long> save(Image image) {
        return databaseHelper.executeSaveOperation(
                ImageQueryMapping.CREATE_ENTITY,
                spec -> spec.bind("url", image.getUrl())
                        .bind("duration", image.getDuration())
                        .bind("type", image.getType())
                        .bind("addedTime", image.getAddedTime()),
                "saving the image"
        );
    }

    public Mono<Boolean> deleteById(Long id) {
        return databaseHelper.executeDeleteOperation(
                ImageQueryMapping.DELETE_ENTITY,
                spec -> spec.bind("id", id),
                "deleting the image"
        );
    }

}
