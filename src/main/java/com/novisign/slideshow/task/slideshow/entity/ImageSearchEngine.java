package com.novisign.slideshow.task.slideshow.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("image_search_engine")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImageSearchEngine {

    @Id
    private Long id;

    @Column("value")
    private String value;

    @Column("type")
    private String type;

    @Column("image_id")
    private Long imageId;

}
