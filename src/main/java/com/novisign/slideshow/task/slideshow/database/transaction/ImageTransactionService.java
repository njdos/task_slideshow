package com.novisign.slideshow.task.slideshow.database.transaction;

import com.novisign.slideshow.task.slideshow.constant.ImageSearchTypes;
import com.novisign.slideshow.task.slideshow.database.repository.ImageRepository;
import com.novisign.slideshow.task.slideshow.database.repository.ImageSearchEngineRepository;
import com.novisign.slideshow.task.slideshow.database.repository.SlideshowImageRepository;
import com.novisign.slideshow.task.slideshow.entity.Image;
import com.novisign.slideshow.task.slideshow.exception.TransactionRollbackException;
import com.novisign.slideshow.task.slideshow.factory.EntityFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@AllArgsConstructor
public class ImageTransactionService {


    private final ImageRepository imageRepository;
    private final ImageSearchEngineRepository imageSearchEngineRepository;
    private final SlideshowImageRepository slideshowImageRepository;
    private final EntityFactory entityFactory;
    private final TransactionalOperator transactionalOperator;


    public Mono<Long> saveNewImageWithKeywords(Image image, List<String> keywords) {
        return transactionalOperator.transactional(
                imageRepository.save(image)
                        .flatMap(savedImageId -> saveKeywords(savedImageId, keywords)
                                .flatMap(keywordsSaved -> {
                                    if (!keywordsSaved) {
                                        return Mono.error(new TransactionRollbackException("Failed to save keywords"));
                                    }

                                    return saveDuration(savedImageId, image.getDuration().toString())
                                            .flatMap(durationSaved -> {
                                                if (!durationSaved) {
                                                    return Mono.error(new TransactionRollbackException("Failed to save duration"));
                                                }
                                                return Mono.just(savedImageId);
                                            });
                                }))
                        .onErrorReturn(-1L)
        );
    }


    public Mono<Boolean> deleteImageById(Long imageId) {
        return transactionalOperator.transactional(
                Mono.zip(
                                slideshowImageRepository.findIdsSlideshowImagesByImageId(imageId).collectList(),
                                imageSearchEngineRepository.findIdsImageSearchByImageId(imageId).collectList()
                        )
                        .flatMap(results -> {
                            List<Long> slideshowIds = results.getT1();
                            List<Long> imageSearchIds = results.getT2();

                            Mono<Void> slideshowDeleteMono = slideshowIds.isEmpty() ? Mono.empty() :
                                    Flux.fromIterable(slideshowIds)
                                            .flatMap(slideshowImageRepository::deleteById)
                                            .then();

                            Mono<Void> imageSearchDeleteMono = imageSearchIds.isEmpty() ? Mono.empty() :
                                    Flux.fromIterable(imageSearchIds)
                                            .flatMap(imageSearchEngineRepository::deleteById)
                                            .then();

                            return Mono.when(slideshowDeleteMono, imageSearchDeleteMono)
                                    .then(imageRepository.deleteById(imageId)
                                    .thenReturn(true));
                        })
                        .onErrorReturn(false)
        );
    }



    private Mono<Boolean> saveKeywords(Long imageId, List<String> keywords) {
        return Flux.fromIterable(keywords)
                .flatMap(keyword -> {
                    var entity = entityFactory.createImageSearchEntity(keyword, ImageSearchTypes.KEYWORD, imageId);
                    return imageSearchEngineRepository.save(entity)
                            .map(id -> id > 0);
                })
                .all(Boolean::booleanValue)
                .defaultIfEmpty(false);
    }

    private Mono<Boolean> saveDuration(Long imageId, String duration) {
        var entity = entityFactory.createImageSearchEntity(duration, ImageSearchTypes.DURATION, imageId);
        return imageSearchEngineRepository.save(entity)
                .map(id -> id > 0)
                .defaultIfEmpty(false);
    }
}
