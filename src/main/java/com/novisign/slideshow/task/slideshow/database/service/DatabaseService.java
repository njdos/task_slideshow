package com.novisign.slideshow.task.slideshow.database.service;

import com.novisign.slideshow.task.slideshow.constant.ImageSearchTypes;
import com.novisign.slideshow.task.slideshow.database.repository.ImageRepository;
import com.novisign.slideshow.task.slideshow.database.repository.ImageSearchEngineRepository;
import com.novisign.slideshow.task.slideshow.entity.Image;
import com.novisign.slideshow.task.slideshow.factory.EntityFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class DatabaseService {

    @Autowired
    public DatabaseService(ImageRepository imageRepository,
                           ImageSearchEngineRepository imageSearchEngineRepository,
                           EntityFactory entityFactory) {
        this.imageRepository = imageRepository;
        this.imageSearchEngineRepository = imageSearchEngineRepository;
        this.entityFactory = entityFactory;
    }

    private final ImageRepository imageRepository;
    private final ImageSearchEngineRepository imageSearchEngineRepository;
    private final EntityFactory entityFactory;

    @Transactional
    public Mono<Boolean> saveNewImageWithKeywords(Image image, List<String> keywords) {
        return imageRepository.save(image)
                .flatMap(imageId -> {
                    if (imageId <= 0) return Mono.just(false);

                    return saveKeywords(imageId, keywords)
                            .flatMap(keywordsSaved -> {
                                if (!keywordsSaved) return Mono.just(false);

                                return saveDuration(imageId, image.getDuration().toString());
                            });
                })
                .onErrorResume(error -> Mono.just(false));
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

}
