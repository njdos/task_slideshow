package com.novisign.slideshow.task.slideshow.database.service;

import com.novisign.slideshow.task.slideshow.database.repository.ImageRepository;
import com.novisign.slideshow.task.slideshow.database.repository.ImageSearchEngineRepository;
import com.novisign.slideshow.task.slideshow.entity.Image;
import com.novisign.slideshow.task.slideshow.service.factory.EntityFactory;
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
                .flatMap(imageId ->
                        Flux.fromIterable(keywords)
                                .flatMap(keyword -> {
                                    var entity = entityFactory.createImageSearchEntity(keyword, "keyword", imageId);
                                    return imageSearchEngineRepository.save(entity);
                                })
                                .then(Mono.just(true))
                )
                .onErrorResume(error -> {
                    System.out.println("Error while saving image and keywords: " + error);
                    return Mono.just(false);
                });
    }

}
