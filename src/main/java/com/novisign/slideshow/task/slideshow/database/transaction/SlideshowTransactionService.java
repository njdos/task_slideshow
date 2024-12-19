package com.novisign.slideshow.task.slideshow.database.transaction;

import com.novisign.slideshow.task.slideshow.database.repository.ProofOfPlayRepository;
import com.novisign.slideshow.task.slideshow.database.repository.SlideshowImageRepository;
import com.novisign.slideshow.task.slideshow.database.repository.SlideshowRepository;
import com.novisign.slideshow.task.slideshow.model.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;

@Service
public class SlideshowTransactionService {

    @Autowired
    public SlideshowTransactionService(SlideshowRepository slideshowRepository,
                                       SlideshowImageRepository slideshowImageRepository,
                                       ProofOfPlayRepository proofOfPlayRepository) {
        this.slideshowRepository = slideshowRepository;
        this.slideshowImageRepository = slideshowImageRepository;
        this.proofOfPlayRepository = proofOfPlayRepository;
    }

    private final SlideshowRepository slideshowRepository;
    private final SlideshowImageRepository slideshowImageRepository;
    private final ProofOfPlayRepository proofOfPlayRepository;

    @Transactional
    public Mono<Boolean> saveNewSlideshow(String name, Map<Long, Integer> correctSlideshow) {
        return slideshowRepository.save(name)
                .flatMap(slideshowId -> {
                    if (slideshowId <= 0) return Mono.just(false);

                    return saveSlideshow(slideshowId, correctSlideshow)
                            .flatMap(Mono::just);
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

    @Transactional
    public Mono<Boolean> deleteSlideshowById(Long slideshowId) {
        return slideshowImageRepository.findIdsSlideshowImagesBySlideshowId(slideshowId)
                .collectList()
                .flatMap(ids -> {
                    if (ids.isEmpty()) {
                        return slideshowRepository.deleteById(slideshowId);
                    } else {
                        return Flux.fromIterable(ids)
                                .flatMap(slideshowImageRepository::deleteById)
                                .collectList()
                                .flatMap(results -> slideshowRepository.deleteById(slideshowId));
                    }
                })
                .onErrorReturn(false);
    }

    public Flux<ApiResponse> slideshowOrder(Long id) {
        return slideshowImageRepository.findIdsSlideshowImagesByImageIds(
                        Collections.singletonList(id)
                )
                .collectList()
                .flatMap(listSlide)
    }


}
