package com.reputon.testtask.controller;

import com.reputon.testtask.dto.ReviewDto;
import com.reputon.testtask.service.ReviewService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping(value = "/{domain}")
    public Mono<ResponseEntity<ReviewDto>> getReview(@PathVariable String domain) {
        return reviewService.getReview(domain);
    }
}
