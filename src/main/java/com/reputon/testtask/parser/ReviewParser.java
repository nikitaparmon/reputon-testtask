package com.reputon.testtask.parser;

import com.reputon.testtask.dto.ReviewDto;

public interface ReviewParser {

    ReviewDto parse(String body);
}
