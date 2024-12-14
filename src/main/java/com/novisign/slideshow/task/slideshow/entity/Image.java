package com.novisign.slideshow.task.slideshow.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("image")
public class Image {

    @Id
    private Long id;

    @Column("url")
    private String url;

    @Column("duration")
    private Integer duration;

    @Column("type")
    private String type;

    @Column("added_time")
    private LocalDateTime addedTime;

    public Image() {
    }

    public Image(String url, Integer duration, String type) {
        this.url = url;
        this.duration = duration;
        this.type = type;
        this.addedTime = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getAddedTime() {
        return addedTime;
    }

    public void setAddedTime(LocalDateTime addedTime) {
        this.addedTime = addedTime;
    }
}
