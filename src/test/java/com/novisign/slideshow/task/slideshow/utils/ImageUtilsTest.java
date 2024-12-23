package com.novisign.slideshow.task.slideshow.utils;

import com.novisign.slideshow.task.slideshow.database.helper.BindConfigurer;
import com.novisign.slideshow.task.slideshow.model.SearchRequest;
import com.novisign.slideshow.task.slideshow.validator.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageUtilsTest {

    @Mock
    private Validator validator;

    @InjectMocks
    private ImageUtils imageUtils;

    private SearchRequest searchRequest;

    @BeforeEach
    void setUp() {
        searchRequest = mock(SearchRequest.class);
    }

    @Test
    void testCreateSearchQuery_withKeyword() {
        lenient().when(searchRequest.keyword()).thenReturn("example");
        lenient().when(searchRequest.duration()).thenReturn(0);
        lenient().when(validator.validateDuration(anyInt())).thenReturn(Mono.just(true));

        Mono<String> queryMono = imageUtils.createSearchQuery(searchRequest);

        queryMono.subscribe(query -> {
            assertNotNull(query);
            assertTrue(query.contains("type = :typeKeyword"));
            assertTrue(query.contains("value ILIKE :value1"));
            assertTrue(query.contains("type = :typeDuration"));
        });
    }

    @Test
    void testCreateSearchQuery_withDuration() {
        when(searchRequest.keyword()).thenReturn(null);
        when(searchRequest.duration()).thenReturn(5);
        when(validator.validateDuration(anyInt())).thenReturn(Mono.just(true));

        Mono<String> queryMono = imageUtils.createSearchQuery(searchRequest);

        queryMono.subscribe(query -> {
            assertNotNull(query);
            assertTrue(query.contains("type = :typeDuration"));
            assertTrue(query.contains("value = :value2"));
        });
    }

    @Test
    void testCreateSearchQuery_withKeywordAndDuration() {
        when(searchRequest.keyword()).thenReturn("example");
        when(searchRequest.duration()).thenReturn(5);
        when(validator.validateDuration(anyInt())).thenReturn(Mono.just(true));

        Mono<String> queryMono = imageUtils.createSearchQuery(searchRequest);

        queryMono.subscribe(query -> {
            assertNotNull(query);
            assertTrue(query.contains("type = :typeKeyword"));
            assertTrue(query.contains("value ILIKE :value1"));
            assertTrue(query.contains("type = :typeDuration"));
            assertTrue(query.contains("value = :value2"));
        });
    }

    @Test
    void testCreateSearchQuery_noConditions() {
        when(searchRequest.keyword()).thenReturn(null);
        when(searchRequest.duration()).thenReturn(0);
        when(validator.validateDuration(anyInt())).thenReturn(Mono.just(false));

        Mono<String> queryMono = imageUtils.createSearchQuery(searchRequest);

        queryMono.subscribe(query -> {
            assertNull(query);
        });
    }

    @Test
    void testBindConfigurer_withKeyword() {
        lenient().when(searchRequest.keyword()).thenReturn("example");
        lenient().when(searchRequest.duration()).thenReturn(null);

        BindConfigurer bindConfigurer = imageUtils.bindConfigurer(searchRequest);

        assertNotNull(bindConfigurer);
    }


    @Test
    void testBindConfigurer_withDuration() {
        lenient().when(searchRequest.keyword()).thenReturn(null);
        lenient().when(searchRequest.duration()).thenReturn(5);

        BindConfigurer bindConfigurer = imageUtils.bindConfigurer(searchRequest);

        assertNotNull(bindConfigurer);
    }

    @Test
    void testBindConfigurer_withKeywordAndDuration() {
        lenient().when(searchRequest.keyword()).thenReturn("example");
        lenient().when(searchRequest.duration()).thenReturn(5);

        BindConfigurer bindConfigurer = imageUtils.bindConfigurer(searchRequest);

        assertNotNull(bindConfigurer);
    }
}
