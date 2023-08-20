package com.magicweb.app.service.mapper;

import com.magicweb.app.domain.Comment;
import com.magicweb.app.domain.WebArticle;
import com.magicweb.app.service.dto.CommentDTO;
import com.magicweb.app.service.dto.WebArticleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Comment} and its DTO {@link CommentDTO}.
 */
@Mapper(componentModel = "spring")
public interface CommentMapper extends EntityMapper<CommentDTO, Comment> {
    @Mapping(target = "webArticle", source = "webArticle", qualifiedByName = "webArticleId")
    CommentDTO toDto(Comment s);

    @Named("webArticleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    WebArticleDTO toDtoWebArticleId(WebArticle webArticle);
}
