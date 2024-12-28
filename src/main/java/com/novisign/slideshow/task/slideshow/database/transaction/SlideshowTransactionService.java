package com.novisign.slideshow.task.slideshow.database.transaction;

import com.novisign.slideshow.task.slideshow.constant.StatusCodes;
import com.novisign.slideshow.task.slideshow.database.repository.ImageRepository;
import com.novisign.slideshow.task.slideshow.database.repository.SlideshowImageRepository;
import com.novisign.slideshow.task.slideshow.database.repository.SlideshowRepository;
import com.novisign.slideshow.task.slideshow.entity.SlideshowImage;
import com.novisign.slideshow.task.slideshow.exception.TransactionRollbackException;
import com.novisign.slideshow.task.slideshow.model.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SlideshowTransactionService {

    private final SlideshowRepository slideshowRepository;
    private final SlideshowImageRepository slideshowImageRepository;
    private final ImageRepository imageRepository;
    private final TransactionalOperator transactionalOperator;

    public Mono<Long> saveNewSlideshow(String name, Map<Long, Integer> correctSlideshow) {
        return Mono.from(transactionalOperator.execute(status ->
                slideshowRepository.save(name)
                        .flatMap(slideshowId -> {
                            if (slideshowId <= 0) {
                                return Mono.error(new TransactionRollbackException("Failed to save slideshow"));
                            }

                            return saveSlideshow(slideshowId, correctSlideshow)
                                    .flatMap(saved -> {
                                        if (!saved) {
                                            return Mono.error(new TransactionRollbackException("Failed to save slideshow images"));
                                        }
                                        return Mono.just(slideshowId);
                                    });
                        })
                        .onErrorResume(error -> Mono.just(-1L))
        ));
    }

    private Mono<Boolean> saveSlideshow(Long slideshowId, Map<Long, Integer> correctSlideshow) {
        return Flux.fromIterable(correctSlideshow.entrySet())
                .flatMap(entry -> slideshowImageRepository.save(slideshowId, entry.getKey(), entry.getValue())
                        .map(savedId -> savedId > 0))
                .all(Boolean::booleanValue)
                .defaultIfEmpty(false);
    }

    public Mono<Boolean> deleteSlideshowById(Long slideshowId) {
        return Mono.from(transactionalOperator.execute(status ->
                slideshowImageRepository.findIdsSlideshowImagesBySlideshowId(slideshowId)
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
                        .onErrorReturn(false)
        ));
    }

    public Flux<ApiResponse> slideshowOrder(Long slideshowId) {
        return slideshowImageRepository.findIdsSlideshowImagesBySlideshowIds(slideshowId)
                .collectList()
                .flatMapMany(slideshowImages -> {
                    List<Long> imageIds = slideshowImages.stream()
                            .map(SlideshowImage::getImageId)
                            .collect(Collectors.toList());

                    return imageRepository.findImageByIds(imageIds)
                            .collectList()
                            .flatMapMany(images -> Flux.fromIterable(slideshowImages)
                                    .flatMap(slideshowImage -> {
                                        Long imageId = slideshowImage.getImageId();
                                        Integer duration = slideshowImage.getDuration();

                                        Map<String, Object> imageData = images.stream()
                                                .filter(image -> image.getId().equals(imageId))
                                                .findFirst()
                                                .map(image -> Map.of(
                                                        "imageId", image.getId(),
                                                        "slideshowId", slideshowImage.getSlideshowId(),
                                                        "imageData", image,
                                                        "duration", duration
                                                ))
                                                .orElse(Map.of());

                                        return Mono.just(ApiResponse.success(StatusCodes.OK, List.of(imageData)));
                                    }));
                });
    }


}
