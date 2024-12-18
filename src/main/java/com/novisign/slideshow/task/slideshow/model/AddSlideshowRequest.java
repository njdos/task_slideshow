package com.novisign.slideshow.task.slideshow.model;

import java.util.List;

public record AddSlideshowRequest(String name, List<TargetImageDuration> images) {
}
