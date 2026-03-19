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

    public ServiceResponse<Categories> getCategoriesByLibelle(String libelle) {
        Categories categorie = daoCategories.selectByLibelle(libelle);
        return ServiceResponse.buildResponse("202","Categories récupérés avec succès", categorie);
    }


    public ServiceResponse<Categories> saveCategories(Categories categorie) {
        Categories foundCategories = daoCategories.selectById(categorie.getId());

            daoCategories.save(categorie);

            if (foundCategories != null){
                return new ServiceResponse<Categories>("2003", "Categorie modifié avec succès", categorie);
            }

            return new ServiceResponse<Categories>("2002", "Categorie créer avec succès", categorie);
        }
    }

