package fr.eni.buymystuff.bo;

import java.time.LocalDateTime;

public class Encheres {

    private int id;
    private Utilisateurs utilisateur;
    private Articles article;
    private LocalDateTime dateEnchere;
    private int montantEnchere;

    public Encheres() {
    }

    public Encheres(int id, Utilisateurs utilisateur, Articles article, LocalDateTime dateEnchere, int montantEnchere) {
        this.id = id;
        this.utilisateur = utilisateur;
        this.article = article;
        this.dateEnchere = dateEnchere;
        this.montantEnchere = montantEnchere;
    }

    @Override
    public String toString() {
        return "Encheres{" +
                "id=" + id +
                ", utilisateur=" + utilisateur +
                ", article=" + article +
                ", dateEnchere=" + dateEnchere +
                ", montantEnchere=" + montantEnchere +
                '}';
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
