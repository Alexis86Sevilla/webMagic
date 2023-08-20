package com.magicweb.app.service;

import com.magicweb.app.domain.*; // for static metamodels
import com.magicweb.app.domain.Card;
import com.magicweb.app.repository.CardRepository;
import com.magicweb.app.service.criteria.CardCriteria;
import com.magicweb.app.service.dto.CardDTO;
import com.magicweb.app.service.mapper.CardMapper;
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
 * Service for executing complex queries for {@link Card} entities in the database.
 * The main input is a {@link CardCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CardDTO} or a {@link Page} of {@link CardDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CardQueryService extends QueryService<Card> {

    private final Logger log = LoggerFactory.getLogger(CardQueryService.class);

    private final CardRepository cardRepository;

    private final CardMapper cardMapper;

    public CardQueryService(CardRepository cardRepository, CardMapper cardMapper) {
        this.cardRepository = cardRepository;
        this.cardMapper = cardMapper;
    }

    /**
     * Return a {@link List} of {@link CardDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CardDTO> findByCriteria(CardCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Card> specification = createSpecification(criteria);
        return cardMapper.toDto(cardRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CardDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CardDTO> findByCriteria(CardCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Card> specification = createSpecification(criteria);
        return cardRepository.findAll(specification, page).map(cardMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CardCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Card> specification = createSpecification(criteria);
        return cardRepository.count(specification);
    }

    /**
     * Function to convert {@link CardCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Card> createSpecification(CardCriteria criteria) {
        Specification<Card> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Card_.id));
            }
            if (criteria.getIdMagic() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIdMagic(), Card_.idMagic));
            }
            if (criteria.getNameEnglish() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNameEnglish(), Card_.nameEnglish));
            }
            if (criteria.getNameSpanish() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNameSpanish(), Card_.nameSpanish));
            }
            if (criteria.getImageUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImageUrl(), Card_.imageUrl));
            }
        }
        return specification;
    }
}
