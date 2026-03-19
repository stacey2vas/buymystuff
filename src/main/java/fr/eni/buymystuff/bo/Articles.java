package fr.eni.buymystuff.bo;

import java.time.LocalDateTime;
import java.util.List;

public class Articles {

    private Long id;
    private String nomArticle;
    private String description;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private int prixInitial;
    private int prixVente;
    private Utilisateurs utilisateur;
    private List<Categories> categories;
    private Adresse adresseProprietaire;
    private List<Encheres> encheres;
    private boolean etatVente;

    public Articles() {
    }

    public Articles(Long id, String nomArticle, String description, LocalDateTime dateDebut, LocalDateTime dateFin, int prixInitial, int prixVente, Utilisateurs utilisateur, List<Categories> categories, Adresse adresseProprietaire, List<Encheres> encheres, boolean etatVente) {
        this.id = id;
        this.nomArticle = nomArticle;
        this.description = description;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.prixInitial = prixInitial;
        this.prixVente = prixVente;
        this.utilisateur = utilisateur;
        this.categories = categories;
        this.adresseProprietaire = adresseProprietaire;
        this.encheres = encheres;
        this.etatVente = etatVente;
    }

    @Override
    public String toString() {
        return "Articles{" +
                "numeroArticle=" + id +
                ", nomArticle='" + nomArticle + '\'' +
                ", description='" + description + '\'' +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", prixInitial=" + prixInitial +
                ", prixVente=" + prixVente +
                ", utilisateur=" + utilisateur +
                ", categorie=" + categories +
                '}';
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

    public Utilisateurs getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateurs utilisateur) {
        this.utilisateur = utilisateur;
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

    public List<Encheres> getEncheres() {
        return encheres;
    }

    public void setEncheres(List<Encheres> encheres) {
        this.encheres = encheres;
    }

    public boolean isEtatVente() {
        return etatVente;
    }

    public void setEtatVente(boolean etatVente) {
        this.etatVente = etatVente;
    }
}
