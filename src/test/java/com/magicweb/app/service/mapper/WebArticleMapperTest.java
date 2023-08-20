package com.magicweb.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WebArticleMapperTest {

    private WebArticleMapper webArticleMapper;

    @BeforeEach
    public void setUp() {
        webArticleMapper = new WebArticleMapperImpl();
    }
}
