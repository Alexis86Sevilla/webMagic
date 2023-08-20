package com.magicweb.app.service.impl;

import com.magicweb.app.domain.WebArticle;
import com.magicweb.app.repository.WebArticleRepository;
import com.magicweb.app.service.WebArticleService;
import com.magicweb.app.service.dto.WebArticleDTO;
import com.magicweb.app.service.mapper.WebArticleMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link WebArticle}.
 */
@Service
@Transactional
public class WebArticleServiceImpl implements WebArticleService {

    private final Logger log = LoggerFactory.getLogger(WebArticleServiceImpl.class);

    private final WebArticleRepository webArticleRepository;

    private final WebArticleMapper webArticleMapper;

    public WebArticleServiceImpl(WebArticleRepository webArticleRepository, WebArticleMapper webArticleMapper) {
        this.webArticleRepository = webArticleRepository;
        this.webArticleMapper = webArticleMapper;
    }

    @Override
    public WebArticleDTO save(WebArticleDTO webArticleDTO) {
        log.debug("Request to save WebArticle : {}", webArticleDTO);
        WebArticle webArticle = webArticleMapper.toEntity(webArticleDTO);
        webArticle = webArticleRepository.save(webArticle);
        return webArticleMapper.toDto(webArticle);
    }

    @Override
    public WebArticleDTO update(WebArticleDTO webArticleDTO) {
        log.debug("Request to update WebArticle : {}", webArticleDTO);
        WebArticle webArticle = webArticleMapper.toEntity(webArticleDTO);
        webArticle = webArticleRepository.save(webArticle);
        return webArticleMapper.toDto(webArticle);
    }

    @Override
    public Optional<WebArticleDTO> partialUpdate(WebArticleDTO webArticleDTO) {
        log.debug("Request to partially update WebArticle : {}", webArticleDTO);

        return webArticleRepository
            .findById(webArticleDTO.getId())
            .map(existingWebArticle -> {
                webArticleMapper.partialUpdate(existingWebArticle, webArticleDTO);

                return existingWebArticle;
            })
            .map(webArticleRepository::save)
            .map(webArticleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WebArticleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WebArticles");
        return webArticleRepository.findAll(pageable).map(webArticleMapper::toDto);
    }

    public Page<WebArticleDTO> findAllWithEagerRelationships(Pageable pageable) {
        return webArticleRepository.findAllWithEagerRelationships(pageable).map(webArticleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WebArticleDTO> findOne(Long id) {
        log.debug("Request to get WebArticle : {}", id);
        return webArticleRepository.findOneWithEagerRelationships(id).map(webArticleMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete WebArticle : {}", id);
        webArticleRepository.deleteById(id);
    }
}
