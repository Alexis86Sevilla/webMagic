package com.magicweb.app.service.impl;

import com.magicweb.app.domain.Tag;
import com.magicweb.app.repository.TagRepository;
import com.magicweb.app.service.TagService;
import com.magicweb.app.service.dto.TagDTO;
import com.magicweb.app.service.mapper.TagMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Tag}.
 */
@Service
@Transactional
public class TagServiceImpl implements TagService {

    private final Logger log = LoggerFactory.getLogger(TagServiceImpl.class);

    private final TagRepository tagRepository;

    private final TagMapper tagMapper;

    public TagServiceImpl(TagRepository tagRepository, TagMapper tagMapper) {
        this.tagRepository = tagRepository;
        this.tagMapper = tagMapper;
    }

    @Override
    public TagDTO save(TagDTO tagDTO) {
        log.debug("Request to save Tag : {}", tagDTO);
        Tag tag = tagMapper.toEntity(tagDTO);
        tag = tagRepository.save(tag);
        return tagMapper.toDto(tag);
    }

    @Override
    public TagDTO update(TagDTO tagDTO) {
        log.debug("Request to update Tag : {}", tagDTO);
        Tag tag = tagMapper.toEntity(tagDTO);
        tag = tagRepository.save(tag);
        return tagMapper.toDto(tag);
    }

    @Override
    public Optional<TagDTO> partialUpdate(TagDTO tagDTO) {
        log.debug("Request to partially update Tag : {}", tagDTO);

        return tagRepository
            .findById(tagDTO.getId())
            .map(existingTag -> {
                tagMapper.partialUpdate(existingTag, tagDTO);

                return existingTag;
            })
            .map(tagRepository::save)
            .map(tagMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TagDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Tags");
        return tagRepository.findAll(pageable).map(tagMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TagDTO> findOne(Long id) {
        log.debug("Request to get Tag : {}", id);
        return tagRepository.findById(id).map(tagMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Tag : {}", id);
        tagRepository.deleteById(id);
    }
}
