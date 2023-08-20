package com.magicweb.app.web.rest;

import com.magicweb.app.repository.WebArticleRepository;
import com.magicweb.app.service.WebArticleQueryService;
import com.magicweb.app.service.WebArticleService;
import com.magicweb.app.service.criteria.WebArticleCriteria;
import com.magicweb.app.service.dto.WebArticleDTO;
import com.magicweb.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.magicweb.app.domain.WebArticle}.
 */
@RestController
@RequestMapping("/api")
public class WebArticleResource {

    private final Logger log = LoggerFactory.getLogger(WebArticleResource.class);

    private static final String ENTITY_NAME = "webArticle";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WebArticleService webArticleService;

    private final WebArticleRepository webArticleRepository;

    private final WebArticleQueryService webArticleQueryService;

    public WebArticleResource(
        WebArticleService webArticleService,
        WebArticleRepository webArticleRepository,
        WebArticleQueryService webArticleQueryService
    ) {
        this.webArticleService = webArticleService;
        this.webArticleRepository = webArticleRepository;
        this.webArticleQueryService = webArticleQueryService;
    }

    /**
     * {@code POST  /web-articles} : Create a new webArticle.
     *
     * @param webArticleDTO the webArticleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new webArticleDTO, or with status {@code 400 (Bad Request)} if the webArticle has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/web-articles")
    public ResponseEntity<WebArticleDTO> createWebArticle(@RequestBody WebArticleDTO webArticleDTO) throws URISyntaxException {
        log.debug("REST request to save WebArticle : {}", webArticleDTO);
        if (webArticleDTO.getId() != null) {
            throw new BadRequestAlertException("A new webArticle cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WebArticleDTO result = webArticleService.save(webArticleDTO);
        return ResponseEntity
            .created(new URI("/api/web-articles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /web-articles/:id} : Updates an existing webArticle.
     *
     * @param id the id of the webArticleDTO to save.
     * @param webArticleDTO the webArticleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated webArticleDTO,
     * or with status {@code 400 (Bad Request)} if the webArticleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the webArticleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/web-articles/{id}")
    public ResponseEntity<WebArticleDTO> updateWebArticle(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody WebArticleDTO webArticleDTO
    ) throws URISyntaxException {
        log.debug("REST request to update WebArticle : {}, {}", id, webArticleDTO);
        if (webArticleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, webArticleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!webArticleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WebArticleDTO result = webArticleService.update(webArticleDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, webArticleDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /web-articles/:id} : Partial updates given fields of an existing webArticle, field will ignore if it is null
     *
     * @param id the id of the webArticleDTO to save.
     * @param webArticleDTO the webArticleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated webArticleDTO,
     * or with status {@code 400 (Bad Request)} if the webArticleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the webArticleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the webArticleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/web-articles/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WebArticleDTO> partialUpdateWebArticle(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody WebArticleDTO webArticleDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update WebArticle partially : {}, {}", id, webArticleDTO);
        if (webArticleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, webArticleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!webArticleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WebArticleDTO> result = webArticleService.partialUpdate(webArticleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, webArticleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /web-articles} : get all the webArticles.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of webArticles in body.
     */
    @GetMapping("/web-articles")
    public ResponseEntity<List<WebArticleDTO>> getAllWebArticles(
        WebArticleCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get WebArticles by criteria: {}", criteria);
        Page<WebArticleDTO> page = webArticleQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /web-articles/count} : count all the webArticles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/web-articles/count")
    public ResponseEntity<Long> countWebArticles(WebArticleCriteria criteria) {
        log.debug("REST request to count WebArticles by criteria: {}", criteria);
        return ResponseEntity.ok().body(webArticleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /web-articles/:id} : get the "id" webArticle.
     *
     * @param id the id of the webArticleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the webArticleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/web-articles/{id}")
    public ResponseEntity<WebArticleDTO> getWebArticle(@PathVariable Long id) {
        log.debug("REST request to get WebArticle : {}", id);
        Optional<WebArticleDTO> webArticleDTO = webArticleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(webArticleDTO);
    }

    /**
     * {@code DELETE  /web-articles/:id} : delete the "id" webArticle.
     *
     * @param id the id of the webArticleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/web-articles/{id}")
    public ResponseEntity<Void> deleteWebArticle(@PathVariable Long id) {
        log.debug("REST request to delete WebArticle : {}", id);
        webArticleService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
