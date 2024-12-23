package com.novisign.slideshow.task.slideshow.processor;

import com.novisign.slideshow.task.slideshow.constant.StatusCodes;
import com.novisign.slideshow.task.slideshow.model.AddSlideshowRequest;
import com.novisign.slideshow.task.slideshow.model.TargetImageDuration;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.List;

class SlideShowProcessorTest {
    private final SlideShowProcessor slideShowProcessor = new SlideShowProcessor();

    @Test
    void testIsValidRequest_withValidRequest() {
        TargetImageDuration image1 = new TargetImageDuration(1L, 5);
        TargetImageDuration image2 = new TargetImageDuration(2L, 10);
        AddSlideshowRequest request = new AddSlideshowRequest("Valid Slideshow", List.of(image1, image2));

        StepVerifier.create(slideShowProcessor.isValidRequest(request))
                .expectNextMatches(response ->
                        response.getCode() == StatusCodes.OK.getCode() &&
                                response.getData().get(0).get("ids").equals(List.of(1L, 2L))
                )
                .verifyComplete();
    }

    @Test
    void testIsValidRequest_withEmptyImages() {
        AddSlideshowRequest request = new AddSlideshowRequest("Empty Images", Collections.emptyList());

        StepVerifier.create(slideShowProcessor.isValidRequest(request))
                .expectNextMatches(response -> response.getCode() == StatusCodes.INVALID_REQUEST_BODY.getCode())
                .verifyComplete();
    }

    @Test
    void testIsValidRequest_withEmptyName() {
        TargetImageDuration image = new TargetImageDuration(1L, 5);
        AddSlideshowRequest request = new AddSlideshowRequest("", List.of(image));

        StepVerifier.create(slideShowProcessor.isValidRequest(request))
                .expectNextMatches(response -> response.getCode() == StatusCodes.INVALID_REQUEST_BODY.getCode())
                .verifyComplete();
    }

    @Test
    void testIsValidRequest_withEmptyNameAndImages() {
        AddSlideshowRequest request = new AddSlideshowRequest("", Collections.emptyList());

        StepVerifier.create(slideShowProcessor.isValidRequest(request))
                .expectNextMatches(response -> response.getCode() == StatusCodes.INVALID_REQUEST_BODY.getCode())
                .verifyComplete();
    }

}