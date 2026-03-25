package fr.eni.buymystuff.DTO;

public class ArticleFilterDTO {

    private String nomArticle;
    private String categorie;
    private Integer prixMin;
    private Integer prixMax;
    private String statut;
    private String selectValue;
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

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getSelectValue() {
        return selectValue;
    }

    public void setSelectValue(String selectValue) {
        this.selectValue = selectValue;
    }
}