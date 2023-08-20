package com.magicweb.app.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.magicweb.app.domain.Card} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CardDTO implements Serializable {

    private Long id;

    private String idMagic;

    private String nameEnglish;

    private String nameSpanish;

    private String imageUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdMagic() {
        return idMagic;
    }

    public void setIdMagic(String idMagic) {
        this.idMagic = idMagic;
    }

    public String getNameEnglish() {
        return nameEnglish;
    }

    public void setNameEnglish(String nameEnglish) {
        this.nameEnglish = nameEnglish;
    }

    public String getNameSpanish() {
        return nameSpanish;
    }

    public void setNameSpanish(String nameSpanish) {
        this.nameSpanish = nameSpanish;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CardDTO)) {
            return false;
        }

        CardDTO cardDTO = (CardDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cardDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CardDTO{" +
            "id=" + getId() +
            ", idMagic='" + getIdMagic() + "'" +
            ", nameEnglish='" + getNameEnglish() + "'" +
            ", nameSpanish='" + getNameSpanish() + "'" +
            ", imageUrl='" + getImageUrl() + "'" +
            "}";
    }
}
