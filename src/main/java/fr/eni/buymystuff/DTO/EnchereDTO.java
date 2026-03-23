package fr.eni.buymystuff.DTO;

import fr.eni.buymystuff.bo.Articles;
import fr.eni.buymystuff.bo.Utilisateurs;

import java.time.LocalDateTime;

public class EnchereDTO {
    private int id;
    private Utilisateurs utilisateur;
    private Articles article;
    private LocalDateTime dateEnchere;
    private int montantEnchere;

    public EnchereDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Utilisateurs getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateurs utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Articles getArticle() {
        return article;
    }

    public void setArticle(Articles article) {
        this.article = article;
    }

    public LocalDateTime getDateEnchere() {
        return dateEnchere;
    }

    public void setDateEnchere(LocalDateTime dateEnchere) {
        this.dateEnchere = dateEnchere;
    }

    public int getMontantEnchere() {
        return montantEnchere;
    }

    public void setMontantEnchere(int montantEnchere) {
        this.montantEnchere = montantEnchere;
    }
}
