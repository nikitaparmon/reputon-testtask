package com.reputon.testtask.service;

import com.reputon.testtask.dto.ReviewDto;
import com.reputon.testtask.exception.DomainNotFoundException;
import com.reputon.testtask.parser.ReviewParser;

import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Service
@AllArgsConstructor
public class TrustpilotReviewService implements ReviewService {
    private static final String TRUSTPILOT_URL = "https://www.trustpilot.com/review/";

    private final WebClient client;
    private final ReviewParser reviewParser;

    @Override
    @Cacheable(value = "domains")
    public Mono<ResponseEntity<ReviewDto>> getReview(String domain) {
        return client.get()
                .uri(TRUSTPILOT_URL + domain)
                .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        return response.bodyToMono(DataBuffer.class)
                                .map(buffer -> {
                                    String string = buffer.toString(StandardCharsets.UTF_8);
                                    DataBufferUtils.release(buffer);
                                    ReviewDto result = reviewParser.parse(string);
                                    return ResponseEntity.ok(result);
                                });
                    } else {
                        return Mono.error(new DomainNotFoundException(domain));
                    }
                });
    }
}
