package com.novisign.slideshow.task.slideshow.utils;

import com.novisign.slideshow.task.slideshow.model.ApiResponse;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class UrlUtils {

    public List<String> extractKeywordsFromUrl(String imageUrl) {
        return Arrays.stream(
                        imageUrl.replaceFirst("https?://(?:www\\.)?", "").split("/"))
                .filter(part -> !part.isBlank())
                .toList();
    }

    public Optional<String> extractImageType(ApiResponse validationResult) {
        try {
            return Optional.ofNullable((String) validationResult.getData().get(0).get("typeImage"));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

}
