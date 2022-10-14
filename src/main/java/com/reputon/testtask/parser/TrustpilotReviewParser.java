package com.reputon.testtask.parser;

import com.reputon.testtask.dto.ReviewDto;

import com.reputon.testtask.exception.HtmlParseException;
import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TrustpilotReviewParser implements ReviewParser {
    private static final String REVIEWS_COUNT_SELECTOR = "#business-unit-title > span.styles_clickable__zQWyh > span";
    private static final String RATING_SELECTOR = "#business-unit-title > div > div > p";

    @Override
    public ReviewDto parse(String body) {
        try {
            Document page = Jsoup.parse(body);
            Element reviewsCountEl = page.selectFirst(REVIEWS_COUNT_SELECTOR);
            Element ratingEl = page.selectFirst(RATING_SELECTOR);

            String reviewsCountStr = reviewsCountEl.text().split(" ")[0];
            Integer reviewsCount = Integer.valueOf(reviewsCountStr);
            Float rating = Float.valueOf(ratingEl.text());
            return ReviewDto.builder()
                    .reviewsCount(reviewsCount)
                    .rating(rating)
                    .build();

        } catch (Exception ex) {
            throw new HtmlParseException();
        }
    }
}
