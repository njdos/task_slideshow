package com.novisign.slideshow.task.slideshow.utils;

import com.novisign.slideshow.task.slideshow.constant.StatusCodes;
import com.novisign.slideshow.task.slideshow.model.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UrlUtilsTest {

    private UrlUtils urlUtils;

    @BeforeEach
    void setUp() {
        urlUtils = new UrlUtils();
    }

    @Test
    void testExtractKeywordsFromUrl_validUrl() {
        String imageUrl = "https://example.com/images/sample-image/keyword1/keyword2?test=some";

        List<String> keywords = urlUtils.extractKeywordsFromUrl(imageUrl);

        assertEquals(5, keywords.size());
        assertEquals("example.com", keywords.get(0));
        assertEquals("images", keywords.get(1));
        assertEquals("sample-image", keywords.get(2));
        assertEquals("keyword1", keywords.get(3));
        assertEquals("keyword2?test=some", keywords.get(4));
    }

    @Test
    void testExtractKeywordsFromUrl_urlWithSubdomain() {
        String imageUrl = "https://sub.example.com/images/sample-image/keyword1";

        List<String> keywords = urlUtils.extractKeywordsFromUrl(imageUrl);

        assertEquals(4, keywords.size());
        assertEquals("sub.example.com", keywords.get(0));
        assertEquals("images", keywords.get(1));
        assertEquals("sample-image", keywords.get(2));
        assertEquals("keyword1", keywords.get(3));
    }

    @Test
    void testExtractKeywordsFromUrl_emptyUrl() {
        String imageUrl = "https://example.com/";

        List<String> keywords = urlUtils.extractKeywordsFromUrl(imageUrl);

        assertEquals("example.com", keywords.get(0));
    }

    @Test
    void testExtractKeywordsFromUrl_noKeywords() {
        String imageUrl = "https://example.com/images/sample-image";

        List<String> keywords = urlUtils.extractKeywordsFromUrl(imageUrl);

        assertEquals(3, keywords.size());
        assertEquals("example.com", keywords.get(0));
        assertEquals("images", keywords.get(1));
        assertEquals("sample-image", keywords.get(2));
    }

    @Test
    void testExtractImageType_validData() {
        ApiResponse response = ApiResponse.success(StatusCodes.OK, List.of(Map.of("typeImage", "image/png")));

        Optional<String> imageType = urlUtils.extractImageType(response);

        assertTrue(imageType.isPresent());
        assertEquals("image/png", imageType.get());
    }

    @Test
    void testExtractImageType_noImageType() {
        ApiResponse response = ApiResponse.success(StatusCodes.OK, List.of());

        Optional<String> imageType = urlUtils.extractImageType(response);

        assertFalse(imageType.isPresent());
    }

}
