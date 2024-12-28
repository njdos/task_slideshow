package com.novisign.slideshow.task.slideshow.database;

import com.novisign.slideshow.task.slideshow.database.helper.BindConfigurer;
import com.novisign.slideshow.task.slideshow.database.queryMapping.DynamicQueryMapping;
import com.novisign.slideshow.task.slideshow.database.repository.ImageRepository;
import com.novisign.slideshow.task.slideshow.database.repository.ProofOfPlayRepository;
import com.novisign.slideshow.task.slideshow.database.transaction.ImageSearchTransactionService;
import com.novisign.slideshow.task.slideshow.database.transaction.ImageTransactionService;
import com.novisign.slideshow.task.slideshow.database.transaction.SlideshowTransactionService;
import com.novisign.slideshow.task.slideshow.entity.Image;
import com.novisign.slideshow.task.slideshow.entity.ProofOfPlay;
import com.novisign.slideshow.task.slideshow.model.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class DatabaseAPI {

    private final ImageTransactionService imageTransactionService;
    private final SlideshowTransactionService slideshowTransactionService;
    private final ImageSearchTransactionService imageSearchTransactionService;
    private final ProofOfPlayRepository proofOfPlayRepository;
    private final ImageRepository imageRepository;

    public Mono<Long> saveNewImage(Image image, List<String> keywords) {
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

    public Mono<Long> saveSlideshow(String name, Map<Long, Integer> correctSlideshow) {
        return slideshowTransactionService.saveNewSlideshow(name, correctSlideshow);
    }

    public Mono<ApiResponse> search(DynamicQueryMapping dynamicQueryMapping, BindConfigurer bindConfigurer) {
        return imageSearchTransactionService.search(dynamicQueryMapping, bindConfigurer);
    }

    public Flux<ApiResponse> slideshowOrder(Long id) {
        return slideshowTransactionService.slideshowOrder(id);
    }

    public Mono<Long> saveProofOfPlay(ProofOfPlay proofOfPlay) {
        return proofOfPlayRepository.save(proofOfPlay);
    }
}
