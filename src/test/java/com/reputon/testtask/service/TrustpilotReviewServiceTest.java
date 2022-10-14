package com.reputon.testtask.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.reputon.testtask.TesttaskApplicationTests;
import com.reputon.testtask.dto.ReviewDto;
import com.reputon.testtask.parser.TrustpilotReviewParser;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = TesttaskApplicationTests.class)
@AutoConfigureMockMvc
class TrustpilotReviewServiceTest {

    public static MockWebServer webServer;

    TrustpilotReviewService reviewService;

    @Mock
    TrustpilotReviewParser reviewParser;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeAll
    static void setUp() throws IOException {
        webServer = new MockWebServer();
        webServer.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        webServer.shutdown();
    }

    @BeforeEach
    void initialize() {
        String baseUrl = String.format("http://localhost:%s", webServer.getPort());
        WebClient webClient = WebClient.create(baseUrl);
        reviewService = new TrustpilotReviewService(webClient, reviewParser);
    }

    @Test
    void isWebClientSendingCorrectRequest() throws InterruptedException {
        //given
        webServer.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.OK.value()));

        //when
        Mono<ReviewDto> reviewDtoMono = reviewService.getReview("test");
        StepVerifier.create(reviewDtoMono)
                .verifyComplete();
        //then

        RecordedRequest recordedRequest = webServer.takeRequest(3, TimeUnit.SECONDS);
        assertEquals("GET", recordedRequest.getMethod());
        assertTrue(recordedRequest.getPath().contains("test"));
    }

    @Test
    void isGettingCorrectReview() throws Exception {
        //given
        ReviewDto reviewDto = ReviewDto.builder()
                .reviewsCount(100)
                .build();

        webServer.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.OK.value())
                .setBody(objectMapper.writeValueAsString(reviewDto)));
        when(reviewParser.parse(anyString())).thenReturn(reviewDto);

        //when
        Mono<ReviewDto> reviewDtoMono = reviewService.getReview("test");

        //then
        StepVerifier.create(reviewDtoMono)
                .expectNextMatches(review -> review.getReviewsCount()
                        .equals(reviewDto.getReviewsCount()))
                .verifyComplete();
    }

}
