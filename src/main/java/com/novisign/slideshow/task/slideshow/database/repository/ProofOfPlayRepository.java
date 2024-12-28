package com.novisign.slideshow.task.slideshow.database.repository;

import com.novisign.slideshow.task.slideshow.database.helper.DatabaseHelper;
import com.novisign.slideshow.task.slideshow.database.queryMapping.ProofOfPlayQueryMapping;
import com.novisign.slideshow.task.slideshow.entity.ProofOfPlay;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
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


}
