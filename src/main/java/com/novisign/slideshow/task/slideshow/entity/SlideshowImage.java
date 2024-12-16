package com.novisign.slideshow.task.slideshow.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("slideshow_image")
@AllArgsConstructor
@NoArgsConstructor
public class SlideshowImage {

    @Id
    private Long id;

    @Column("slideshow_id")
    private Long slideshowId;

    @Column("image_id")
    private Long imageId;

    @Column("duration")
    private Integer duration;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSlideshowId() {
        return slideshowId;
    }

    public void setSlideshowId(Long slideshowId) {
        this.slideshowId = slideshowId;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }
}
