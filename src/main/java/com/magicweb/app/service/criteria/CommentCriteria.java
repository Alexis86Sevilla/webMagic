package com.magicweb.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.magicweb.app.domain.Comment} entity. This class is used
 * in {@link com.magicweb.app.web.rest.CommentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /comments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CommentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter body;

    private InstantFilter date;

    private LongFilter webArticleId;

    private Boolean distinct;

    public CommentCriteria() {}

    public CommentCriteria(CommentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.body = other.body == null ? null : other.body.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.webArticleId = other.webArticleId == null ? null : other.webArticleId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CommentCriteria copy() {
        return new CommentCriteria(this);
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

    public StringFilter getBody() {
        return body;
    }

    public StringFilter body() {
        if (body == null) {
            body = new StringFilter();
        }
        return body;
    }

    public void setBody(StringFilter body) {
        this.body = body;
    }

    public InstantFilter getDate() {
        return date;
    }

    public InstantFilter date() {
        if (date == null) {
            date = new InstantFilter();
        }
        return date;
    }

    public void setDate(InstantFilter date) {
        this.date = date;
    }

    public LongFilter getWebArticleId() {
        return webArticleId;
    }

    public LongFilter webArticleId() {
        if (webArticleId == null) {
            webArticleId = new LongFilter();
        }
        return webArticleId;
    }

    public void setWebArticleId(LongFilter webArticleId) {
        this.webArticleId = webArticleId;
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
        final CommentCriteria that = (CommentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(body, that.body) &&
            Objects.equals(date, that.date) &&
            Objects.equals(webArticleId, that.webArticleId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, body, date, webArticleId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommentCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (body != null ? "body=" + body + ", " : "") +
            (date != null ? "date=" + date + ", " : "") +
            (webArticleId != null ? "webArticleId=" + webArticleId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
