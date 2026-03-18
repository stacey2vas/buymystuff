package fr.eni.buymystuff.DTO;


import fr.eni.buymystuff.bo.Adresse;
import fr.eni.buymystuff.bo.Categories;
import fr.eni.buymystuff.bo.Encheres;
import fr.eni.buymystuff.bo.Utilisateurs;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ArticleFormDTO {

    private Long id; // pour l'édition, sinon null pour la création
    private String nomArticle;
    private String description;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private int prixInitial;
    private int prixVente;
    private List<Categories> categories;
    private Adresse adresseProprietaire;
    private MultipartFile imageFile; // champ pour upload


    public ArticleFormDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomArticle() {
        return nomArticle;
    }

    public void setNomArticle(String nomArticle) {
        this.nomArticle = nomArticle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDateTime getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDateTime dateFin) {
        this.dateFin = dateFin;
    }

    public int getPrixInitial() {
        return prixInitial;
    }

    public void setPrixInitial(int prixInitial) {
        this.prixInitial = prixInitial;
    }

    public int getPrixVente() {
        return prixVente;
    }

    public void setPrixVente(int prixVente) {
        this.prixVente = prixVente;
    }

    public List<Categories> getCategories() {
        return categories;
    }

    public void setCategories(List<Categories> categories) {
        this.categories = categories;
    }

    public Adresse getAdresseProprietaire() {
        return adresseProprietaire;
    }

    public void setAdresseProprietaire(Adresse adresseProprietaire) {
        this.adresseProprietaire = adresseProprietaire;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }
}