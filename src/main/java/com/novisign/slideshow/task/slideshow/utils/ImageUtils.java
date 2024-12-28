package com.novisign.slideshow.task.slideshow.utils;

import com.novisign.slideshow.task.slideshow.constant.ImageSearchTypes;
import com.novisign.slideshow.task.slideshow.database.helper.BindConfigurer;
import com.novisign.slideshow.task.slideshow.model.SearchRequest;
import com.novisign.slideshow.task.slideshow.validator.Validator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class ImageUtils {

    private final Validator validator;

    public Mono<String> createSearchQuery(SearchRequest searchRequest) {
        StringBuilder queryBuilder = new StringBuilder("SELECT image_id AS id FROM image_search_engine WHERE ");

        boolean[] isFirstCondition = {true};

        if (searchRequest.keyword() != null && !searchRequest.keyword().isEmpty()) {
            if (!isFirstCondition[0]) queryBuilder.append(" OR ");
            queryBuilder.append("type = LOWER(:typeKeyword) AND value ILIKE :value1");
            isFirstCondition[0] = false;
        }

        return validator.validateDuration(searchRequest.duration())
                .flatMap(isValid -> {
                    if (isValid) {
                        if (!isFirstCondition[0]) queryBuilder.append(" OR ");
                        queryBuilder.append("type = LOWER(:typeDuration) AND value = :value2");
                        isFirstCondition[0] = false;
                    }

                    if (isFirstCondition[0]) {
                        return Mono.empty();
                    }

                    return Mono.just(queryBuilder.toString());
                });
    }

    public BindConfigurer bindConfigurer(SearchRequest searchRequest) {
        return spec -> {
            if (searchRequest.keyword() != null && !searchRequest.keyword().isEmpty()) {
                spec = spec.bind("typeKeyword", ImageSearchTypes.KEYWORD)
                        .bind("value1", searchRequest.keyword());
            }

            if (searchRequest.duration() != null && searchRequest.duration() > 0) {
                spec = spec.bind("typeDuration", ImageSearchTypes.DURATION)
                        .bind("value2", searchRequest.duration().toString());
            }

            return spec;
        };
    }
}
