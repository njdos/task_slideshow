package com.novisign.slideshow.task.slideshow.database.repository;

import com.novisign.slideshow.task.slideshow.database.helper.DatabaseHelper;
import com.novisign.slideshow.task.slideshow.database.queryMapping.SlideshowQueryMapping;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Repository
@AllArgsConstructor
public class SlideshowRepository {

    private final DatabaseHelper databaseHelper;

    public Mono<Long> save(String name) {
        return databaseHelper.executeSaveOperation(
                SlideshowQueryMapping.CREATE_ENTITY,
                spec -> spec
                        .bind("name", name)
                        .bind("created_time", LocalDateTime.now()),
                "saving slideshow"
        );
    }

    public Mono<Boolean> deleteById(Long id) {
        return databaseHelper.executeDeleteOperation(
                SlideshowQueryMapping.DELETE_ENTITY,
                spec -> spec.bind("id", id),
                "deleting the slideshow"
        );
    }
}
