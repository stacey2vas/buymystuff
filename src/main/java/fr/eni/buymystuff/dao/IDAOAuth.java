package fr.eni.buymystuff.dao;


import fr.eni.buymystuff.bo.Adresse;
import fr.eni.buymystuff.bo.Utilisateurs;

public interface IDAOAuth {

    public Adresse insertAdresse(Adresse adresse);
    public Utilisateurs insert(Utilisateurs utilisateur);
    public Utilisateurs selectById(int id);
}
