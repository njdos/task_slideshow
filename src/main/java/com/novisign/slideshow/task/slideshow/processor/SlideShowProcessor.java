package com.novisign.slideshow.task.slideshow.processor;

import com.novisign.slideshow.task.slideshow.constant.StatusCodes;
import com.novisign.slideshow.task.slideshow.model.AddSlideshowRequest;
import com.novisign.slideshow.task.slideshow.model.ApiResponse;
import com.novisign.slideshow.task.slideshow.model.TargetImageDuration;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SlideShowProcessor {

    public Mono<ApiResponse> isValidRequest(AddSlideshowRequest request) {
        if (request.images().isEmpty() || request.name().isEmpty()) {
            return Mono.just(ApiResponse.error(StatusCodes.INVALID_REQUEST_BODY));
        }

        List<Long> ids = convertIdsToList(request.images());
        if (ids.isEmpty()) {
            return Mono.just(ApiResponse.error(StatusCodes.INVALID_REQUEST_BODY));
        }

        return Mono.just(ApiResponse.success(StatusCodes.OK, List.of(Map.of("ids", ids))));
    }

    private List<Long> convertIdsToList(List<TargetImageDuration> images) {
        return images.stream()
                .map(TargetImageDuration::image_id)
                .collect(Collectors.toList());
    }

}
