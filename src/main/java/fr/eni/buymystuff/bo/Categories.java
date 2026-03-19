package fr.eni.buymystuff.bo;

public class Categories {
    private int id;
    private String libelle;

    public Categories() {
    }

    public Categories(int id, String libelle) {
        this.id = (int) id;
        this.libelle = libelle;
    }

    @Override
    public String toString() {
        return "Categories{" +
                "numeroCategorie=" + id +
                ", libelle='" + libelle + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = (int) id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
}

