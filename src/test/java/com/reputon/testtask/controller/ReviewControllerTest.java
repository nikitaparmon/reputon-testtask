package com.reputon.testtask.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.reputon.testtask.TesttaskApplicationTests;

import com.reputon.testtask.dto.ReviewDto;
import com.reputon.testtask.exception.DomainNotFoundException;
import com.reputon.testtask.exception.RemoteHostException;
import com.reputon.testtask.service.ReviewService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = ReviewController.class)
@ContextConfiguration(classes = TesttaskApplicationTests.class)
class ReviewControllerTest {

    @MockBean
    ReviewService reviewService;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void shouldGetReviewWithStatusOk() {
        //given
        ReviewDto reviewDto = ReviewDto.builder()
                .reviewsCount(100)
                .rating(5.0F)
                .build();
        Mono<ReviewDto> reviewDtoMono = Mono.just(reviewDto);
        when(reviewService.getReview(anyString())).thenReturn(reviewDtoMono);

        // when
        // then
        webTestClient.get()
                .uri("/reviews/test")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ReviewDto.class)
                .value(ReviewDto::getReviewsCount, Matchers.equalTo(reviewDto.getReviewsCount()));

        verify(reviewService, times(1)).getReview("test");
        verifyNoMoreInteractions(reviewService);
    }

    @Test
    void shouldGetReviewWithStatusNotFound() {
        //given
        when(reviewService.getReview(anyString())).thenThrow(new DomainNotFoundException("test"));

        // when
        // then
        webTestClient.get()
                .uri("/reviews/test")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class);

        verify(reviewService, times(1)).getReview("test");
        verifyNoMoreInteractions(reviewService);
    }

    @Test
    void shouldGetReviewWithStatusUnavailable() {
        //given
        when(reviewService.getReview(anyString())).thenThrow(new RemoteHostException("testHost"));

        // when
        // then
        webTestClient.get()
                .uri("/reviews/test")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody(String.class);

        verify(reviewService, times(1)).getReview("test");
        verifyNoMoreInteractions(reviewService);
    }
}
