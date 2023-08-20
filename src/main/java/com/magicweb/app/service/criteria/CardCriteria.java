package com.magicweb.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.magicweb.app.domain.Card} entity. This class is used
 * in {@link com.magicweb.app.web.rest.CardResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cards?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CardCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter idMagic;

    private StringFilter nameEnglish;

    private StringFilter nameSpanish;

    private StringFilter imageUrl;

    private Boolean distinct;

    public CardCriteria() {}

    public CardCriteria(CardCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.idMagic = other.idMagic == null ? null : other.idMagic.copy();
        this.nameEnglish = other.nameEnglish == null ? null : other.nameEnglish.copy();
        this.nameSpanish = other.nameSpanish == null ? null : other.nameSpanish.copy();
        this.imageUrl = other.imageUrl == null ? null : other.imageUrl.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CardCriteria copy() {
        return new CardCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getIdMagic() {
        return idMagic;
    }

    public StringFilter idMagic() {
        if (idMagic == null) {
            idMagic = new StringFilter();
        }
        return idMagic;
    }

    public void setIdMagic(StringFilter idMagic) {
        this.idMagic = idMagic;
    }

    public StringFilter getNameEnglish() {
        return nameEnglish;
    }

    public StringFilter nameEnglish() {
        if (nameEnglish == null) {
            nameEnglish = new StringFilter();
        }
        return nameEnglish;
    }

    public void setNameEnglish(StringFilter nameEnglish) {
        this.nameEnglish = nameEnglish;
    }

    public StringFilter getNameSpanish() {
        return nameSpanish;
    }

    public StringFilter nameSpanish() {
        if (nameSpanish == null) {
            nameSpanish = new StringFilter();
        }
        return nameSpanish;
    }

    public void setNameSpanish(StringFilter nameSpanish) {
        this.nameSpanish = nameSpanish;
    }

    public StringFilter getImageUrl() {
        return imageUrl;
    }

    public StringFilter imageUrl() {
        if (imageUrl == null) {
            imageUrl = new StringFilter();
        }
        return imageUrl;
    }

    public void setImageUrl(StringFilter imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CardCriteria that = (CardCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(idMagic, that.idMagic) &&
            Objects.equals(nameEnglish, that.nameEnglish) &&
            Objects.equals(nameSpanish, that.nameSpanish) &&
            Objects.equals(imageUrl, that.imageUrl) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idMagic, nameEnglish, nameSpanish, imageUrl, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CardCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (idMagic != null ? "idMagic=" + idMagic + ", " : "") +
            (nameEnglish != null ? "nameEnglish=" + nameEnglish + ", " : "") +
            (nameSpanish != null ? "nameSpanish=" + nameSpanish + ", " : "") +
            (imageUrl != null ? "imageUrl=" + imageUrl + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
