package fr.eni.buymystuff.bo;

public class Categories {
    private Long id;
    private String libelle;

    public Categories() {
    }

    public Categories(Long id, String libelle) {
        this.id = (Long) id;
        this.libelle = libelle;
    }

    @Override
    public String toString() {
        return "Categories{" +
                "ID=" + id +
                ", libelle='" + libelle + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = (Long) id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
}

