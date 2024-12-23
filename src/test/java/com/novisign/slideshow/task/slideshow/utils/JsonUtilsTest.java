package com.novisign.slideshow.task.slideshow.utils;

import com.novisign.slideshow.task.slideshow.constant.StatusCodes;
import com.novisign.slideshow.task.slideshow.exception.JsonSerializationException;
import com.novisign.slideshow.task.slideshow.model.ApiResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JsonUtilsTest {
    private final JsonUtils jsonUtils = new JsonUtils();

    @Test
    void toJson_validObject_returnsJsonString() {
        ApiResponse response = ApiResponse.error(StatusCodes.SERIALIZATION_ERROR);

        String json = jsonUtils.toJson(response);

        assertEquals("{\"status\":\"error\",\"code\":6003,\"message\":\"JSON serialization error occurred\",\"data\":[]}", json);
    }

    @Test
    void toJson_nullObject_throwsJsonSerializationException() {
        Object nullObject = null;

        JsonSerializationException exception = assertThrows(
                JsonSerializationException.class,
                () -> jsonUtils.toJson(nullObject),
                "Expected toJson(null) to throw, but it didn't."
        );

        assertTrue(exception.getMessage().contains("Cannot serialize null object to JSON"));
    }

    @Test
    void toJson_objectWithInvalidField_throwsJsonSerializationException() {
        Object invalidObject = new Object() {
            private final Object selfReference = this;
        };

        JsonSerializationException exception = assertThrows(
                JsonSerializationException.class,
                () -> jsonUtils.toJson(invalidObject),
                "Expected toJson(invalidObject) to throw, but it didn't."
        );

        assertTrue(exception.getMessage().contains("Error serializing object to JSON"));
    }
}