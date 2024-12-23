package com.novisign.slideshow.task.slideshow.factory;

import com.novisign.slideshow.task.slideshow.entity.ImageSearchEngine;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EntityFactoryTest {

    private final EntityFactory entityFactory = new EntityFactory();

    @Test
    void createImageSearchEntity_ShouldReturnProperlyInitializedEntity() {
        String keyword = "nature";
        String type = "landscape";
        Long imageId = 123L;

        ImageSearchEngine result = entityFactory.createImageSearchEntity(keyword, type, imageId);

        assertThat(result).isNotNull();
        assertThat(result.getValue()).isEqualTo(keyword);
        assertThat(result.getType()).isEqualTo(type);
        assertThat(result.getImageId()).isEqualTo(imageId);
    }
}