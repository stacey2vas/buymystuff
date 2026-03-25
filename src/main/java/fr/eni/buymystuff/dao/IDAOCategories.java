package fr.eni.buymystuff.dao;

import fr.eni.buymystuff.bo.Categories;

import java.util.List;

public interface IDAOCategories {

    public List<Categories> selectAllCategories();
    public Categories save(Categories categories);
    public Categories selectByLibelle(String libelle);
    public Categories selectById(Long id);
    public Categories update(Categories categories);
}
