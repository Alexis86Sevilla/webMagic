package com.magicweb.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Tag.
 */
@Entity
@Table(name = "tag")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Tag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "tags")
    @JsonIgnoreProperties(value = { "comments", "tags" }, allowSetters = true)
    private Set<WebArticle> articles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Tag id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Tag name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<WebArticle> getArticles() {
        return this.articles;
    }

    public void setArticles(Set<WebArticle> webArticles) {
        if (this.articles != null) {
            this.articles.forEach(i -> i.removeTags(this));
        }
        if (webArticles != null) {
            webArticles.forEach(i -> i.addTags(this));
        }
        this.articles = webArticles;
    }

    public Tag articles(Set<WebArticle> webArticles) {
        this.setArticles(webArticles);
        return this;
    }

    public Tag addArticles(WebArticle webArticle) {
        this.articles.add(webArticle);
        webArticle.getTags().add(this);
        return this;
    }

    public Tag removeArticles(WebArticle webArticle) {
        this.articles.remove(webArticle);
        webArticle.getTags().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tag)) {
            return false;
        }
        return id != null && id.equals(((Tag) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tag{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
