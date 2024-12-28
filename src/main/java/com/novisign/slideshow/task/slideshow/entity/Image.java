package com.novisign.slideshow.task.slideshow.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("image")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    public Image(String url, Integer duration, String type) {
        this.url = url;
        this.duration = duration;
        this.type = type;
        this.addedTime = LocalDateTime.now(); // автоматически устанавливается время добавления
    }
}
