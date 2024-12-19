package com.novisign.slideshow.task.slideshow.database.transaction;

import com.novisign.slideshow.task.slideshow.constant.StatusCodes;
import com.novisign.slideshow.task.slideshow.database.repository.ImageRepository;
import com.novisign.slideshow.task.slideshow.database.repository.ImageSearchEngineRepository;
import com.novisign.slideshow.task.slideshow.database.repository.SlideshowImageRepository;
import com.novisign.slideshow.task.slideshow.entity.SlideshowImage;
import com.novisign.slideshow.task.slideshow.model.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ImageSearchTransactionService {

    @Autowired
    public ImageSearchTransactionService(ImageSearchEngineRepository imageSearchEngineRepository,
                                         SlideshowImageRepository slideshowImageRepository,
                                         ImageRepository imageRepository) {
        this.imageSearchEngineRepository = imageSearchEngineRepository;
        this.slideshowImageRepository = slideshowImageRepository;
        this.imageRepository = imageRepository;
    }

    private final ImageSearchEngineRepository imageSearchEngineRepository;
    private final SlideshowImageRepository slideshowImageRepository;
    private final ImageRepository imageRepository;

    public Mono<ApiResponse> search(String keyword, Integer duration) {
        return imageSearchEngineRepository.search(keyword, duration)
                .collectList()
                .flatMap(foundImageIds -> {
                    if (foundImageIds.isEmpty()) {
                        return Mono.just(ApiResponse.success(StatusCodes.OK, Collections.emptyList()));
                    }

                    return imageRepository.findImageByIds(foundImageIds)
                            .collectList()
                            .flatMap(images ->
                                    slideshowImageRepository.findIdsSlideshowImagesBySlideshowIds(foundImageIds)
                                            .collectList()
                                            .map(slideshowImages -> {
                                                Map<Long, List<SlideshowImage>> groupedSlideshowImages = slideshowImages.stream()
                                                        .collect(Collectors.groupingBy(SlideshowImage::getImageId));

                                                List<Map<String, Object>> result = images.stream()
                                                        .map(image -> {
                                                            Map<String, Object> resultMap = new HashMap<>();
                                                            resultMap.put("image", image);

                                                            List<SlideshowImage> slides = groupedSlideshowImages.getOrDefault(image.getId(), Collections.emptyList());
                                                            resultMap.put("slideshow", slides);

                                                            return resultMap;
                                                        })
                                                        .collect(Collectors.toList());

                                                return ApiResponse.success(StatusCodes.OK, result);
                                            })
                            );
                });
    }
}
