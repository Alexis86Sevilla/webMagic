package com.magicweb.app.service.mapper;

import com.magicweb.app.domain.Tag;
import com.magicweb.app.domain.WebArticle;
import com.magicweb.app.service.dto.TagDTO;
import com.magicweb.app.service.dto.WebArticleDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WebArticle} and its DTO {@link WebArticleDTO}.
 */
@Mapper(componentModel = "spring")
public interface WebArticleMapper extends EntityMapper<WebArticleDTO, WebArticle> {
    @Mapping(target = "tags", source = "tags", qualifiedByName = "tagIdSet")
    WebArticleDTO toDto(WebArticle s);

    @Mapping(target = "removeTags", ignore = true)
    WebArticle toEntity(WebArticleDTO webArticleDTO);

    @Named("tagId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TagDTO toDtoTagId(Tag tag);

    @Named("tagIdSet")
    default Set<TagDTO> toDtoTagIdSet(Set<Tag> tag) {
        return tag.stream().map(this::toDtoTagId).collect(Collectors.toSet());
    }
}
