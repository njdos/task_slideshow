package com.novisign.slideshow.task.slideshow.handler;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class FrontendHandler {

    public Mono<ServerResponse> loadHtmlFile(ServerRequest request) {
        try {
            Resource resource = new ClassPathResource("ui/index.html");

            return ServerResponse.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .bodyValue(resource);
        } catch (Exception e) {
            return ServerResponse.status(500).bodyValue("Error loading the page");
        }
    }


}
