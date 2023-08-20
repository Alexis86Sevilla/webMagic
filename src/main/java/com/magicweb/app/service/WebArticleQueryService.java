package com.magicweb.app.service;

import com.magicweb.app.domain.*; // for static metamodels
import com.magicweb.app.domain.WebArticle;
import com.magicweb.app.repository.WebArticleRepository;
import com.magicweb.app.service.criteria.WebArticleCriteria;
import com.magicweb.app.service.dto.WebArticleDTO;
import com.magicweb.app.service.mapper.WebArticleMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link WebArticle} entities in the database.
 * The main input is a {@link WebArticleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link WebArticleDTO} or a {@link Page} of {@link WebArticleDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WebArticleQueryService extends QueryService<WebArticle> {

    private final Logger log = LoggerFactory.getLogger(WebArticleQueryService.class);

    private final WebArticleRepository webArticleRepository;

    private final WebArticleMapper webArticleMapper;

    public WebArticleQueryService(WebArticleRepository webArticleRepository, WebArticleMapper webArticleMapper) {
        this.webArticleRepository = webArticleRepository;
        this.webArticleMapper = webArticleMapper;
    }

    /**
     * Return a {@link List} of {@link WebArticleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<WebArticleDTO> findByCriteria(WebArticleCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<WebArticle> specification = createSpecification(criteria);
        return webArticleMapper.toDto(webArticleRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link WebArticleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<WebArticleDTO> findByCriteria(WebArticleCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<WebArticle> specification = createSpecification(criteria);
        return webArticleRepository.findAll(specification, page).map(webArticleMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(WebArticleCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<WebArticle> specification = createSpecification(criteria);
        return webArticleRepository.count(specification);
    }

    /**
     * Function to convert {@link WebArticleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<WebArticle> createSpecification(WebArticleCriteria criteria) {
        Specification<WebArticle> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), WebArticle_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), WebArticle_.title));
            }
            if (criteria.getBody() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBody(), WebArticle_.body));
            }
            if (criteria.getAuthor() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAuthor(), WebArticle_.author));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), WebArticle_.date));
            }
            if (criteria.getCommentsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCommentsId(),
                            root -> root.join(WebArticle_.comments, JoinType.LEFT).get(Comment_.id)
                        )
                    );
            }
            if (criteria.getTagsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTagsId(), root -> root.join(WebArticle_.tags, JoinType.LEFT).get(Tag_.id))
                    );
            }
        }
        return specification;
    }
}
