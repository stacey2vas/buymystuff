package fr.eni.buymystuff.dao;

import fr.eni.buymystuff.bo.Encheres;

import java.util.List;

public interface IDAOEnchere {
    List<Encheres> selectEncheresByArticleId(Long id);
}
