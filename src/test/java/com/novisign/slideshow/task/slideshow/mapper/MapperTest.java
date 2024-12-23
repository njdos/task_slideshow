package com.novisign.slideshow.task.slideshow.mapper;

import com.novisign.slideshow.task.slideshow.entity.Image;
import com.novisign.slideshow.task.slideshow.entity.SlideshowImage;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MapperTest {

    @Test
    void testMapRowToImage() {
        Row row = mock(Row.class);
        RowMetadata rowMetadata = mock(RowMetadata.class);

        when(row.get("id", Long.class)).thenReturn(1L);
        when(row.get("url", String.class)).thenReturn("http://example.com/image.jpg");
        when(row.get("duration", Integer.class)).thenReturn(10);
        when(row.get("type", String.class)).thenReturn("jpeg");
        when(row.get("added_time", LocalDateTime.class)).thenReturn(LocalDateTime.of(2023, 12, 23, 12, 0));

        Image image = Mapper.mapRowToImage.apply(row, rowMetadata);

        assertNotNull(image);
        assertEquals(1L, image.getId());
        assertEquals("http://example.com/image.jpg", image.getUrl());
        assertEquals(10, image.getDuration());
        assertEquals("jpeg", image.getType());
        assertEquals(LocalDateTime.of(2023, 12, 23, 12, 0), image.getAddedTime());
    }

    @Test
    void testMapRowToImageIdAndDuration() {
        Row row = mock(Row.class);
        RowMetadata rowMetadata = mock(RowMetadata.class);

        when(row.get("id", Long.class)).thenReturn(2L);
        when(row.get("duration", Integer.class)).thenReturn(15);

        Image image = Mapper.mapRowToImageIdAndDuration.apply(row, rowMetadata);

        assertNotNull(image);
        assertEquals(2L, image.getId());
        assertEquals(15, image.getDuration());
    }

    @Test
    void testMapRowToSlideshowImage() {
        Row row = mock(Row.class);
        RowMetadata rowMetadata = mock(RowMetadata.class);

        when(row.get("id", Long.class)).thenReturn(3L);
        when(row.get("slideshow_id", Long.class)).thenReturn(101L);
        when(row.get("image_id", Long.class)).thenReturn(202L);
        when(row.get("duration", Integer.class)).thenReturn(20);

        SlideshowImage slideshowImage = Mapper.mapRowToSlideshowImage.apply(row, rowMetadata);

        assertNotNull(slideshowImage);
        assertEquals(3L, slideshowImage.getId());
        assertEquals(101L, slideshowImage.getSlideshowId());
        assertEquals(202L, slideshowImage.getImageId());
        assertEquals(20, slideshowImage.getDuration());
    }

    @Test
    void testMapRowToId() {
        Row row = mock(Row.class);
        RowMetadata rowMetadata = mock(RowMetadata.class);

        when(row.get("id", Long.class)).thenReturn(42L);

        Long id = Mapper.mapRowToId.apply(row, rowMetadata);

        assertNotNull(id);
        assertEquals(42L, id);
    }
}