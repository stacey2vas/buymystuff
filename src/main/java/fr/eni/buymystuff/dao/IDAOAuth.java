package fr.eni.buymystuff.dao;


import fr.eni.buymystuff.bo.Utilisateurs;

public interface IDAOAuth {

    public Utilisateurs selectByEmailAndPassword(String email, String password);
}
