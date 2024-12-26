package com.novisign.slideshow.task.slideshow.database.transaction;

import com.novisign.slideshow.task.slideshow.constant.ImageSearchTypes;
import com.novisign.slideshow.task.slideshow.database.repository.ImageRepository;
import com.novisign.slideshow.task.slideshow.database.repository.ImageSearchEngineRepository;
import com.novisign.slideshow.task.slideshow.entity.Image;
import com.novisign.slideshow.task.slideshow.exception.TransactionRollbackException;
import com.novisign.slideshow.task.slideshow.factory.EntityFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class ImageTransactionService {

    @Autowired
    public ImageTransactionService(ImageRepository imageRepository,
                                   ImageSearchEngineRepository imageSearchEngineRepository,
                                   EntityFactory entityFactory,
                                   TransactionalOperator transactionalOperator) {
        this.imageRepository = imageRepository;
        this.imageSearchEngineRepository = imageSearchEngineRepository;
        this.entityFactory = entityFactory;
        this.transactionalOperator = transactionalOperator;
    }

    private final ImageRepository imageRepository;
    private final ImageSearchEngineRepository imageSearchEngineRepository;

    private final EntityFactory entityFactory;
    private final TransactionalOperator transactionalOperator;


    public Mono<Long> saveNewImageWithKeywords(Image image, List<String> keywords) {
        return Mono.from(transactionalOperator.execute(status ->
                imageRepository.save(image)
                        .flatMap(imageId -> {
                            if (imageId <= 0) {
                                return Mono.error(new TransactionRollbackException("Failed to save image"));
                            }


                            return saveKeywords(imageId, keywords)
                                    .flatMap(keywordsSaved -> {
                                        if (!keywordsSaved) {
                                            return Mono.error(new TransactionRollbackException("Failed to save keywords"));
                                        }

                                        return saveDuration(imageId, image.getDuration().toString())
                                                .flatMap(durationSaved -> {
                                                    if (!durationSaved) {
                                                        return Mono.error(new TransactionRollbackException("Failed to save duration"));
                                                    }
                                                    return Mono.just(imageId);
                                                });
                                    });
                        })
                        .onErrorResume(error -> Mono.just(-1L))
        ));
    }


    @Transactional
    public Mono<Boolean> deleteImageById(Long imageId) {
        return imageSearchEngineRepository.findIdsImageSearchByImageId(imageId)
                .collectList()
                .flatMap(ids -> {
                    if (ids.isEmpty()) {
                        return imageRepository.deleteById(imageId);
                    } else {
                        return Flux.fromIterable(ids)
                                .flatMap(imageSearchEngineRepository::deleteById)
                                .collectList()
                                .flatMap(results -> imageRepository.deleteById(imageId));
                    }
                })
                .onErrorReturn(false);
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
