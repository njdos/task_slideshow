package com.novisign.slideshow.task.slideshow.handler;


import com.novisign.slideshow.task.slideshow.constant.StatusCodes;
import com.novisign.slideshow.task.slideshow.model.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@WebFluxTest
@Import(SwaggerHandler.class)
class SwaggerHandlerTest {
    @Autowired
    private WebTestClient webTestClient;

    @InjectMocks
    private SwaggerHandler swaggerHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetSwaggerYaml_success() throws IOException {
        ClassPathResource mockResource = mock(ClassPathResource.class);
        when(mockResource.getFile()).thenReturn(new ClassPathResource("swagger.yaml").getFile());
        String fileContent = Files.readString(mockResource.getFile().toPath());

        webTestClient.get()
                .uri("/swagger.yaml")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.TEXT_PLAIN)
                .expectBody(String.class).isEqualTo(fileContent);
    }

    @Test
    void testGetSwaggerYaml_fileNotFound() {
        Mono<String> fileReadingMono = Mono.error(new IOException("File not found"));

        when(swaggerHandler.getSwaggerYaml(null)).thenReturn(
                fileReadingMono.flatMap(content -> ServerResponse.ok()
                        .contentType(MediaType.TEXT_PLAIN)
                        .bodyValue(content))
        );

        webTestClient.get()
                .uri("/swagger.yaml")
                .exchange()
                .expectStatus().isEqualTo(500)
                .expectBody(ApiResponse.class)
                .value(response -> assertEquals(StatusCodes.LOADING_FILE_FAILED, response.getCode()));
    }

    @Test
    void testGetSwaggerYaml_serverError() {
        webTestClient.get()
                .uri("/swagger.yaml")
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody(ApiResponse.class)
                .value(response -> assertEquals(StatusCodes.LOADING_FILE_FAILED, response.getCode()));
    }
}