package com.magicweb.app.service.mapper;

import com.magicweb.app.domain.Card;
import com.magicweb.app.service.dto.CardDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Card} and its DTO {@link CardDTO}.
 */
@Mapper(componentModel = "spring")
public interface CardMapper extends EntityMapper<CardDTO, Card> {}
