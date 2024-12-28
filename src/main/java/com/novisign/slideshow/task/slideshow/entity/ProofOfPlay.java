package com.novisign.slideshow.task.slideshow.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("proof_of_play")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

}
