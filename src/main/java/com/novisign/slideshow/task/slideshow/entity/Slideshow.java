package com.novisign.slideshow.task.slideshow.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("slideshow")
@Getter
@Setter
@AllArgsConstructor
public class Slideshow {

    @Id
    private Long id;

    @Column("name")
    private String name;

    @Column("created_time")
    private LocalDateTime createdTime;

    public Slideshow() {
        this.createdTime = LocalDateTime.now();
    }

}
