package com.magicweb.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.magicweb.app.domain.WebArticle} entity. This class is used
 * in {@link com.magicweb.app.web.rest.WebArticleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /web-articles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WebArticleCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private StringFilter body;

    private StringFilter author;

    private InstantFilter date;

    private LongFilter commentsId;

    private LongFilter tagsId;

    private Boolean distinct;

    public WebArticleCriteria() {}

    public WebArticleCriteria(WebArticleCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.body = other.body == null ? null : other.body.copy();
        this.author = other.author == null ? null : other.author.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.commentsId = other.commentsId == null ? null : other.commentsId.copy();
        this.tagsId = other.tagsId == null ? null : other.tagsId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public WebArticleCriteria copy() {
        return new WebArticleCriteria(this);
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

    public StringFilter getTitle() {
        return title;
    }

    public StringFilter title() {
        if (title == null) {
            title = new StringFilter();
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
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

    public StringFilter getAuthor() {
        return author;
    }

    public StringFilter author() {
        if (author == null) {
            author = new StringFilter();
        }
        return author;
    }

    public void setAuthor(StringFilter author) {
        this.author = author;
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

    public LongFilter getCommentsId() {
        return commentsId;
    }

    public LongFilter commentsId() {
        if (commentsId == null) {
            commentsId = new LongFilter();
        }
        return commentsId;
    }

    public void setCommentsId(LongFilter commentsId) {
        this.commentsId = commentsId;
    }

    public LongFilter getTagsId() {
        return tagsId;
    }

    public LongFilter tagsId() {
        if (tagsId == null) {
            tagsId = new LongFilter();
        }
        return tagsId;
    }

    public void setTagsId(LongFilter tagsId) {
        this.tagsId = tagsId;
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
        final WebArticleCriteria that = (WebArticleCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(body, that.body) &&
            Objects.equals(author, that.author) &&
            Objects.equals(date, that.date) &&
            Objects.equals(commentsId, that.commentsId) &&
            Objects.equals(tagsId, that.tagsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, body, author, date, commentsId, tagsId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WebArticleCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (title != null ? "title=" + title + ", " : "") +
            (body != null ? "body=" + body + ", " : "") +
            (author != null ? "author=" + author + ", " : "") +
            (date != null ? "date=" + date + ", " : "") +
            (commentsId != null ? "commentsId=" + commentsId + ", " : "") +
            (tagsId != null ? "tagsId=" + tagsId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
