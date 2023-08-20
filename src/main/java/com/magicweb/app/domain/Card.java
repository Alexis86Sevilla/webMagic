package com.magicweb.app.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * A Card.
 */
@Entity
@Table(name = "card")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Card implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "id_magic")
    private String idMagic;

    @Column(name = "name_english")
    private String nameEnglish;

    @Column(name = "name_spanish")
    private String nameSpanish;

    @Column(name = "image_url")
    private String imageUrl;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Card id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdMagic() {
        return this.idMagic;
    }

    public Card idMagic(String idMagic) {
        this.setIdMagic(idMagic);
        return this;
    }

    public void setIdMagic(String idMagic) {
        this.idMagic = idMagic;
    }

    public String getNameEnglish() {
        return this.nameEnglish;
    }

    public Card nameEnglish(String nameEnglish) {
        this.setNameEnglish(nameEnglish);
        return this;
    }

    public void setNameEnglish(String nameEnglish) {
        this.nameEnglish = nameEnglish;
    }

    public String getNameSpanish() {
        return this.nameSpanish;
    }

    public Card nameSpanish(String nameSpanish) {
        this.setNameSpanish(nameSpanish);
        return this;
    }

    public void setNameSpanish(String nameSpanish) {
        this.nameSpanish = nameSpanish;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public Card imageUrl(String imageUrl) {
        this.setImageUrl(imageUrl);
        return this;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Card)) {
            return false;
        }
        return id != null && id.equals(((Card) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Card{" +
            "id=" + getId() +
            ", idMagic='" + getIdMagic() + "'" +
            ", nameEnglish='" + getNameEnglish() + "'" +
            ", nameSpanish='" + getNameSpanish() + "'" +
            ", imageUrl='" + getImageUrl() + "'" +
            "}";
    }
}
