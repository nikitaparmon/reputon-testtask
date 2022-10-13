package com.reputon.testtask.service;

import com.reputon.testtask.dto.ReviewDto;
import reactor.core.publisher.Mono;

public interface ReviewService {

    Mono<ReviewDto> getReview(String domain);
}
