package com.reputon.testtask.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.reputon.testtask.dto.ReviewDto;
import com.reputon.testtask.exception.HtmlParseException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TrustpilotReviewParserTest {

    @InjectMocks
    ReviewParser trustpilotReviewParser = new TrustpilotReviewParser();

    @Mock
    Document document;

    @Mock
    Element element;

    @Mock
    MockedStatic<Jsoup> jsoup;


    @Test
    void parseCorrectHtml() {
        //given
        String elementsText = "100";

        jsoup.when(() -> Jsoup.parse(anyString())).thenReturn(document);
        when(document.selectFirst(anyString())).thenReturn(element);
        when(element.text()).thenReturn(elementsText);

        //when
        ReviewDto reviewDto = trustpilotReviewParser.parse("random string");

        //then
        assertEquals(100, reviewDto.getReviewsCount());
        assertEquals(100F, reviewDto.getRating());
    }

    @Test
    void parseIncorrectHtml() {
        //given
        //when
        //then
        Assertions.assertThrows(HtmlParseException.class, () -> trustpilotReviewParser.parse("random string"));
    }
}
