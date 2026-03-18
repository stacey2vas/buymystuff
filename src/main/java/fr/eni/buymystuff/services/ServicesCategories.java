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

    public ServiceResponse<Categories> saveCategories(Categories categories) {

        Categories existingByLibelle = daoCategories.selectByLibelle(categories.getLibelle());

        if (existingByLibelle != null && existingByLibelle.getId() != categories.getId()) {
            return new ServiceResponse<>("4001", "Une catégorie avec ce libellé existe déjà", null);
        }

        Categories foundCategorie = daoCategories.selectById(categories.getId());

        if (foundCategorie == null){
            daoCategories.save(categories);
            return new ServiceResponse<>("2002", "Catégorie créée avec succès", categories);

        } else {
            foundCategorie.setLibelle(categories.getLibelle());
            daoCategories.save(foundCategorie);
            return new ServiceResponse<>("2003", "Catégorie modifiée avec succès", foundCategorie);
        }
    }

    }
