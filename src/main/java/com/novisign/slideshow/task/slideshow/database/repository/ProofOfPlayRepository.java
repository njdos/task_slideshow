package com.novisign.slideshow.task.slideshow.database.repository;

import com.novisign.slideshow.task.slideshow.database.helper.DatabaseHelper;
import com.novisign.slideshow.task.slideshow.database.queryMapping.ProofOfPlayQueryMapping;
import com.novisign.slideshow.task.slideshow.entity.ProofOfPlay;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@AllArgsConstructor
public class ProofOfPlayRepository {

    private final DatabaseHelper databaseHelper;


    public Mono<Long> save(ProofOfPlay proofOfPlay) {
        return databaseHelper.executeSaveOperation(
                ProofOfPlayQueryMapping.CREATE_ENTITY,
                spec -> spec
                        .bind("slideshow_id", proofOfPlay.getSlideshowId())
                        .bind("image_id", proofOfPlay.getImageId())
                        .bind("play_time", proofOfPlay.getPlayTime()),
                "saving slideshow image"
        );
    }

    public Mono<Boolean> deleteById(Long id) {
        return databaseHelper.executeDeleteOperation(
                ProofOfPlayQueryMapping.DELETE_ENTITY,
                spec -> spec.bind("id", id),
                "deleting proof of play search engine"
        );
    }

    public Flux<Long> findIdsProofOfPlayByImageId(Long imageId) {
        return databaseHelper.executeForMany(
                ProofOfPlayQueryMapping.GET_PK_BY_IMAGE_ID,
                spec -> spec.bind("image_id", imageId),
                "fetching pk proof of play by image id"
        );
    }

    public Flux<Long> findIdsProofOfPlayBySlideshowId(Long slideshowId) {
        return databaseHelper.executeForMany(
                ProofOfPlayQueryMapping.GET_PK_BY_SLIDESHOW_ID,
                spec -> spec.bind("slideshow_id", slideshowId),
                "fetching pk proof of play by image id"
        );
    }

}
