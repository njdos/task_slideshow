package com.novisign.slideshow.task.slideshow.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.novisign.slideshow.task.slideshow.exception.JsonSerializationException;
import org.springframework.stereotype.Component;

@Component
public class JsonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public String toJson(Object object) {
        if (object == null) {
            throw new JsonSerializationException("Cannot serialize null object to JSON");
        }
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new JsonSerializationException("Error serializing object to JSON", e);
        }
    }

}
