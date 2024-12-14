package com.novisign.slideshow.task.slideshow.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("proof_of_play")
@AllArgsConstructor
@NoArgsConstructor
public class ProofOfPlay {

    @Id
    private Long id;

    @Column("slideshow_id")
    private Long slideshowId;

    @Column("image_id")
    private Long imageId;

    @Column("play_time")
    private LocalDateTime playTime;

    public ProofOfPlay(Long slideshowId, Long imageId) {
        this.slideshowId = slideshowId;
        this.imageId = imageId;
        this.playTime = LocalDateTime.now();
    }

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

    public LocalDateTime getPlayTime() {
        return playTime;
    }

    public void setPlayTime(LocalDateTime playTime) {
        this.playTime = playTime;
    }
}
