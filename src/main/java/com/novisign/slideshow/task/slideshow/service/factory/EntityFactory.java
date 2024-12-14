package com.novisign.slideshow.task.slideshow.service.factory;

import com.novisign.slideshow.task.slideshow.entity.ImageSearchEngine;
import org.springframework.stereotype.Component;

@Component
public class EntityFactory {

    public ImageSearchEngine createImageSearchEntity(String keyword, String type, Long imageId) {
        ImageSearchEngine entity = new ImageSearchEngine();
        entity.setValue(keyword);
        entity.setType(type);
        entity.setImageId(imageId);
        return entity;
    }
}
