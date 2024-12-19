package com.novisign.slideshow.task.slideshow.utils;

import com.novisign.slideshow.task.slideshow.constant.ImageSearchTypes;
import com.novisign.slideshow.task.slideshow.database.helper.BindConfigurer;
import com.novisign.slideshow.task.slideshow.model.SearchRequest;
import com.novisign.slideshow.task.slideshow.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ImageUtils {

    @Autowired
    public ImageUtils(Validator validator) {
        this.validator = validator;
    }

    private final Validator validator;

    public Mono<String> createSearchQuery(SearchRequest searchRequest) {
        StringBuilder queryBuilder = new StringBuilder("SELECT image_id AS id FROM image_search_engine WHERE ");

        boolean isFirstCondition = true;

        if (searchRequest.keyword() != null && !searchRequest.keyword().isEmpty()) {
            queryBuilder.append("type = :typeKeyword AND value ILIKE :value1");
            isFirstCondition = false;
        }

        boolean finalIsFirstCondition = isFirstCondition;
        return validator.validateDuration(searchRequest.duration())
                .flatMap(isValid -> {
                    if (isValid) {
                        if (!finalIsFirstCondition) {
                            queryBuilder.append(" AND ");
                        }
                        queryBuilder.append("type = :typeDuration AND value = :value2");
                    }

                    if (finalIsFirstCondition) {
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
