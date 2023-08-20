package com.magicweb.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.magicweb.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WebArticleDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WebArticleDTO.class);
        WebArticleDTO webArticleDTO1 = new WebArticleDTO();
        webArticleDTO1.setId(1L);
        WebArticleDTO webArticleDTO2 = new WebArticleDTO();
        assertThat(webArticleDTO1).isNotEqualTo(webArticleDTO2);
        webArticleDTO2.setId(webArticleDTO1.getId());
        assertThat(webArticleDTO1).isEqualTo(webArticleDTO2);
        webArticleDTO2.setId(2L);
        assertThat(webArticleDTO1).isNotEqualTo(webArticleDTO2);
        webArticleDTO1.setId(null);
        assertThat(webArticleDTO1).isNotEqualTo(webArticleDTO2);
    }
}
