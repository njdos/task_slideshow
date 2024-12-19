package com.novisign.slideshow.task.slideshow.database;

import com.novisign.slideshow.task.slideshow.database.repository.ImageRepository;
import com.novisign.slideshow.task.slideshow.database.transaction.ImageSearchTransactionService;
import com.novisign.slideshow.task.slideshow.database.transaction.ImageTransactionService;
import com.novisign.slideshow.task.slideshow.database.transaction.SlideshowTransactionService;
import com.novisign.slideshow.task.slideshow.entity.Image;
import com.novisign.slideshow.task.slideshow.model.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
public class DatabaseAPI {

    @Autowired
    public DatabaseAPI(ImageTransactionService imageTransactionService,
                       SlideshowTransactionService slideshowTransactionService,
                       ImageSearchTransactionService imageSearchTransactionService,
                       ImageRepository imageRepository) {
        this.imageTransactionService = imageTransactionService;
        this.slideshowTransactionService = slideshowTransactionService;
        this.imageSearchTransactionService = imageSearchTransactionService;
        this.imageRepository = imageRepository;
    }

    private final ImageTransactionService imageTransactionService;
    private final SlideshowTransactionService slideshowTransactionService;
    private final ImageSearchTransactionService imageSearchTransactionService;
    private final ImageRepository imageRepository;

    public Mono<Boolean> saveNewImage(Image image, List<String> keywords) {
        return imageTransactionService.saveNewImageWithKeywords(image, keywords);
    }

    public Mono<Image> findImageByUrl(String imageUrl) {
        return imageRepository.findImageByUrl(imageUrl);
    }

    public Mono<Boolean> deleteImageById(Long id) {
        return imageTransactionService.deleteImageById(id);
    }

    public Mono<Boolean> deleteSlideshowById(Long id) {
        return slideshowTransactionService.deleteSlideshowById(id);
    }

    public Flux<Image> findImageIdAndDurationByIds(List<Long> ids) {
        return imageRepository.findImageIdAndDurationByIds(ids);
    }

    public Mono<Boolean> saveSlideshow(String name, Map<Long, Integer> correctSlideshow) {
        return slideshowTransactionService.saveNewSlideshow(name, correctSlideshow);
    }

    public Mono<ApiResponse> search(String keyword, Integer duration) {
        return imageSearchTransactionService.search(keyword, duration);
    }

}
