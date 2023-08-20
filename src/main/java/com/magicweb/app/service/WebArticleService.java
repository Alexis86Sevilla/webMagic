package com.magicweb.app.service;

import com.magicweb.app.service.dto.WebArticleDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.magicweb.app.domain.WebArticle}.
 */
public interface WebArticleService {
    /**
     * Save a webArticle.
     *
     * @param webArticleDTO the entity to save.
     * @return the persisted entity.
     */
    WebArticleDTO save(WebArticleDTO webArticleDTO);

    /**
     * Updates a webArticle.
     *
     * @param webArticleDTO the entity to update.
     * @return the persisted entity.
     */
    WebArticleDTO update(WebArticleDTO webArticleDTO);

    /**
     * Partially updates a webArticle.
     *
     * @param webArticleDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<WebArticleDTO> partialUpdate(WebArticleDTO webArticleDTO);

    /**
     * Get all the webArticles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WebArticleDTO> findAll(Pageable pageable);

    /**
     * Get all the webArticles with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WebArticleDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" webArticle.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WebArticleDTO> findOne(Long id);

    /**
     * Delete the "id" webArticle.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
