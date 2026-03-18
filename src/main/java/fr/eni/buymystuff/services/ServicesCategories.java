package fr.eni.buymystuff.services;

import fr.eni.buymystuff.bo.Categories;
import fr.eni.buymystuff.jdbc.IDAOCategories;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ServicesCategories {

    private final IDAOCategories daoCategories;

    public ServicesCategories(IDAOCategories daoCategories) {
        this.daoCategories = daoCategories;
    }

    public ServiceResponse<List<Categories>> getCategoriesCatalog() {

        List<Categories> categories = daoCategories.selectAllCategories();

        return ServiceResponse.buildResponse("202","Categories récupérés avec succès", categories);
    }
    public ServiceResponse<Categories> saveCategories(Categories categories) {

        Categories foundCategorie  = daoCategories.selectById(categories.getId());

        Categories savedCategorie = daoCategories.save(categories);

        if (foundCategorie != null){
            return new ServiceResponse<Categories>("2003", "Categorie modifié avec succès", categories);
        }

        return new ServiceResponse<Categories>("2002", "Categorie créer avec succès", categories);
    }

}
