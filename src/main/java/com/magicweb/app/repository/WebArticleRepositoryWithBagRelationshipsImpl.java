package com.magicweb.app.repository;

import com.magicweb.app.domain.WebArticle;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class WebArticleRepositoryWithBagRelationshipsImpl implements WebArticleRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<WebArticle> fetchBagRelationships(Optional<WebArticle> webArticle) {
        return webArticle.map(this::fetchTags);
    }

    @Override
    public Page<WebArticle> fetchBagRelationships(Page<WebArticle> webArticles) {
        return new PageImpl<>(fetchBagRelationships(webArticles.getContent()), webArticles.getPageable(), webArticles.getTotalElements());
    }

    @Override
    public List<WebArticle> fetchBagRelationships(List<WebArticle> webArticles) {
        return Optional.of(webArticles).map(this::fetchTags).orElse(Collections.emptyList());
    }

    WebArticle fetchTags(WebArticle result) {
        return entityManager
            .createQuery(
                "select webArticle from WebArticle webArticle left join fetch webArticle.tags where webArticle is :webArticle",
                WebArticle.class
            )
            .setParameter("webArticle", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<WebArticle> fetchTags(List<WebArticle> webArticles) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, webArticles.size()).forEach(index -> order.put(webArticles.get(index).getId(), index));
        List<WebArticle> result = entityManager
            .createQuery(
                "select distinct webArticle from WebArticle webArticle left join fetch webArticle.tags where webArticle in :webArticles",
                WebArticle.class
            )
            .setParameter("webArticles", webArticles)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
