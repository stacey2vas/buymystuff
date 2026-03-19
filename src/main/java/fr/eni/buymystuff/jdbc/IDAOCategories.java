package fr.eni.buymystuff.jdbc;

import fr.eni.buymystuff.bo.Categories;

import java.util.List;

public interface IDAOCategories {

    public List<Categories> selectAllCategories();
    //public Categories selectCategoryById(int id);
    public Categories save(Categories categories);
    public Categories selectByLibelle(String libelle);

    Categories selectById(int id);
    //public Categories update(Categories categories);
    //public Categories delete(Categories categories);
}
