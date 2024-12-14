package com.novisign.slideshow.task.slideshow.service.image;

import com.novisign.slideshow.task.slideshow.database.DatabaseAPI;
import com.novisign.slideshow.task.slideshow.entity.Image;
import com.novisign.slideshow.task.slideshow.exchange.Fetcher;
import com.novisign.slideshow.task.slideshow.exchange.RequestBuilder;
import com.novisign.slideshow.task.slideshow.model.request.AddImageRequest;
import com.novisign.slideshow.task.slideshow.model.responce.ApiResponse;
import com.novisign.slideshow.task.slideshow.validator.ImageValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ImageService {

    private final DatabaseAPI databaseAPI;
    private final ImageValidator imageValidator;
    private final Fetcher fetcher;

    @Autowired
    public ImageService(DatabaseAPI databaseAPI, ImageValidator imageValidator, Fetcher fetcher) {
        this.databaseAPI = databaseAPI;
        this.imageValidator = imageValidator;
        this.fetcher = fetcher;
    }

    public Mono<ApiResponse> addImage(AddImageRequest request) {
        return databaseAPI.findByUrl(request.url())
                .hasElement()
                .flatMap(exists -> exists
                        ? Mono.just(ApiResponse.error(4000))
                        : processNewImage(request)
                )
                .onErrorResume(error -> Mono.just(ApiResponse.error(5001)));
    }

    private Mono<ApiResponse> processNewImage(AddImageRequest request) {
        String imageUrl = request.url();
        RequestBuilder imageRequest = RequestBuilder.builder(imageUrl, HttpMethod.HEAD);

        return fetcher.fetchRequest(imageRequest)
                .flatMap(clientResponse -> validateImage(clientResponse, request.duration()))
                .flatMap(validationResult -> {
                    if (validationResult.getCode() != 200) {
                        return Mono.just(validationResult);
                    }
                    return saveImageWithKeywords(imageUrl, request.duration(), validationResult);
                })
                .onErrorResume(error -> Mono.just(ApiResponse.error(5001)));
    }

    private Mono<ApiResponse> validateImage(ClientResponse clientResponse, int duration) {
        return imageValidator.validate(Mono.just(clientResponse), duration)
                .onErrorResume(error -> Mono.just(ApiResponse.error(3000)));
    }

    private Mono<ApiResponse> saveImageWithKeywords(String imageUrl, int duration, ApiResponse validationResult) {
        var imageType = extractImageType(validationResult);
        if (imageType.isEmpty()) {
            return Mono.just(ApiResponse.error(1001));
        }

        Image image = new Image(imageUrl, duration, imageType.get());
        List<String> keywords = extractKeywordsFromUrl(imageUrl);

        return databaseAPI.saveNewImage(image, keywords)
                .map(savedImage -> ApiResponse.success(201, Collections.emptyList()))
                .onErrorResume(error -> Mono.just(ApiResponse.error(5001)));
    }

    private List<String> extractKeywordsFromUrl(String imageUrl) {
        String domainStrippedUrl = imageUrl.replaceFirst("https?://(?:www\\.)?", "");
        return List.of(domainStrippedUrl.split("/"));
    }

    private Optional<String> extractImageType(ApiResponse validationResult) {
        try {
            return Optional.ofNullable((String) validationResult.getData().get(0).get("typeImage"));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
