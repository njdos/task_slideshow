package com.novisign.slideshow.task.slideshow.database;

import com.novisign.slideshow.task.slideshow.database.repository.ImageRepository;
import com.novisign.slideshow.task.slideshow.database.service.DatabaseTransactionService;
import com.novisign.slideshow.task.slideshow.entity.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class DatabaseAPI {

    @Autowired
    public DatabaseAPI(DatabaseTransactionService databaseTransactionService, ImageRepository imageRepository) {
        this.databaseTransactionService = databaseTransactionService;
        this.imageRepository = imageRepository;
    }

    private final DatabaseTransactionService databaseTransactionService;
    private final ImageRepository imageRepository;

    public Mono<Boolean> saveNewImage(Image image, List<String> keywords) {
        return databaseTransactionService.saveNewImageWithKeywords(image, keywords);
    }

    public Mono<Image> findImageByUrl(String imageUrl) {
        return imageRepository.findImageByUrl(imageUrl);
    }

    public Mono<Boolean> deleteImageById(Long id) {
        return databaseTransactionService.deleteImageById(id);
    }

}
