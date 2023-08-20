package com.magicweb.app.repository;

import com.magicweb.app.domain.WebArticle;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface WebArticleRepositoryWithBagRelationships {
    Optional<WebArticle> fetchBagRelationships(Optional<WebArticle> webArticle);

    List<WebArticle> fetchBagRelationships(List<WebArticle> webArticles);

    Page<WebArticle> fetchBagRelationships(Page<WebArticle> webArticles);
}
