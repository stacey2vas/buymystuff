package fr.eni.buymystuff.DTO;

import java.time.LocalDate;

public class ArticleFilterDTO {

    private String nomArticle;
    private String categorie;
    private Integer prixMin;
    private Integer prixMax;
    private LocalDate dateStart;
    private LocalDate dateEnd;

    // ----- Getters et Setters -----
    public String getNomArticle() {
        return nomArticle;
    }

    public void setNomArticle(String nomArticle) {
        this.nomArticle = nomArticle;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public Integer getPrixMin() {
        return prixMin;
    }

    public void setPrixMin(Integer prixMin) {
        this.prixMin = prixMin;
    }

    public Integer getPrixMax() {
        return prixMax;
    }

    public void setPrixMax(Integer prixMax) {
        this.prixMax = prixMax;
    }

    public LocalDate getDateStart() {
        return dateStart;
    }

    public void setDateStart(LocalDate dateStart) {
        this.dateStart = dateStart;
    }

    public LocalDate getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(LocalDate dateEnd) {
        this.dateEnd = dateEnd;
    }
}