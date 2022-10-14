package com.reputon.testtask.service;

import com.reputon.testtask.dto.ReviewDto;
import com.reputon.testtask.exception.DomainNotFoundException;
import com.reputon.testtask.exception.RemoteHostException;
import com.reputon.testtask.parser.ReviewParser;

import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Service
@AllArgsConstructor
public class TrustpilotReviewService implements ReviewService {
    private static final String TRUSTPILOT_URL = "www.trustpilot.com/review/";

    private final WebClient client;
    private final ReviewParser reviewParser;

    @Override
    @Cacheable(value = "domains")
    public Mono<ReviewDto> getReview(String domain) {
        return client.get()
                .uri(TRUSTPILOT_URL + domain)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        clientResponse -> Mono.error(new DomainNotFoundException(domain))
                )
                .onStatus(HttpStatus::is5xxServerError,
                        clientResponse -> Mono.error(new RemoteHostException(TRUSTPILOT_URL))
                )
                .bodyToMono(DataBuffer.class)
                .map(buffer -> {
                    String string = buffer.toString(StandardCharsets.UTF_8);
                    DataBufferUtils.release(buffer);
                    return reviewParser.parse(string);
                });
    }
}
