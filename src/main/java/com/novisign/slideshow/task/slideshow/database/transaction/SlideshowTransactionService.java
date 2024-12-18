package com.novisign.slideshow.task.slideshow.database.transaction;

import com.novisign.slideshow.task.slideshow.database.repository.SlideshowImageRepository;
import com.novisign.slideshow.task.slideshow.database.repository.SlideshowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class SlideshowTransactionService {

    @Autowired
    public SlideshowTransactionService(SlideshowRepository slideshowRepository, SlideshowImageRepository slideshowImageRepository) {
        this.slideshowRepository = slideshowRepository;
        this.slideshowImageRepository = slideshowImageRepository;
    }

    private final SlideshowRepository slideshowRepository;
    private final SlideshowImageRepository slideshowImageRepository;

    @Transactional
    public Mono<Boolean> saveNewSlideshow(String name, Map<Long, Integer> correctSlideshow) {
        return slideshowRepository.save(name)
                .flatMap(slideshowId -> {
                    if (slideshowId <= 0) return Mono.just(false);

                    return saveSlideshow(slideshowId, correctSlideshow)
                            .flatMap(slideshowSaved -> Mono.just(slideshowSaved));
                })
                .onErrorResume(error -> Mono.just(false));
    }

    private Mono<Boolean> saveSlideshow(Long slideshowId, Map<Long, Integer> correctSlideshow) {
        return Flux.fromIterable(correctSlideshow.entrySet())
                .flatMap(entry -> slideshowImageRepository.save(slideshowId, entry.getKey(), entry.getValue())
                        .map(savedId -> savedId > 0))
                .all(Boolean::booleanValue)
                .defaultIfEmpty(false);
    }

}
