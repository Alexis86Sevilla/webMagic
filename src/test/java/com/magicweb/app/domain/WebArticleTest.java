package com.magicweb.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.magicweb.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WebArticleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WebArticle.class);
        WebArticle webArticle1 = new WebArticle();
        webArticle1.setId(1L);
        WebArticle webArticle2 = new WebArticle();
        webArticle2.setId(webArticle1.getId());
        assertThat(webArticle1).isEqualTo(webArticle2);
        webArticle2.setId(2L);
        assertThat(webArticle1).isNotEqualTo(webArticle2);
        webArticle1.setId(null);
        assertThat(webArticle1).isNotEqualTo(webArticle2);
    }
}
