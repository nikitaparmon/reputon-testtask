package com.reputon.testtask.service;

import com.reputon.testtask.dto.ReviewDto;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface ReviewService {

    Mono<ResponseEntity<ReviewDto>> getReview(String domain);
}
