package com.novisign.slideshow.task.slideshow.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("slideshow_image")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SlideshowImage {

    @Id
    private Long id;

    @Column("slideshow_id")
    private Long slideshowId;

    @Column("image_id")
    private Long imageId;

    @Column("duration")
    private Integer duration;


}
