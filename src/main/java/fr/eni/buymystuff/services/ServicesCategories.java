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

    public ServiceResponse <List<Categories>> getCategoriesCatalog() {
        List<Categories> categories = daoCategories.selectAllCategories();
        return new ServiceResponse<>("2000", "Categorie récupérés avec succès", categories);
    }

    public ServiceResponse <Categories> getCategoriesByLibelle(String libelle) {
        Categories categorie = daoCategories.selectByLibelle(libelle);
        if(categorie == null){
            return new ServiceResponse<>("2005", "Categorie n'existe pas", null);
        } else {
            return new ServiceResponse<>("2005", "Categorie bien récupérée", categorie);
        }
    }

    public ServiceResponse <Categories> selectById(Long id) {

        Categories categorie = daoCategories.selectById(id);
        if(categorie == null) {
            return new ServiceResponse<>("2005", "ID fourni n'existe pas", null);
        }
        return new ServiceResponse<>("2002", "Categorie récupérée avec succès", categorie);
    }
    public ServiceResponse<Categories> create(Categories categorie) {
        daoCategories.save(categorie);
        return new ServiceResponse<>("2002", "Categorie créée avec succès", categorie);
    }

    public ServiceResponse<Categories> update(Categories categorie) {
        daoCategories.save(categorie);
        return new ServiceResponse<>("2003", "Categorie modifiée avec succès", categorie);
    }

    public ServiceResponse<Categories> saveCategories(Categories categorie) {

        if (categorie.getId() == null) {
            ServiceResponse<Categories> categorieExist = this.getCategoriesByLibelle(categorie.getLibelle());

            if (categorieExist.getData() == null) {
                daoCategories.save(categorie);
                return new ServiceResponse<Categories>("2004", "Categorie créée avec succès", categorie);
            } else {
                return new ServiceResponse<Categories>("2005", "Categorie existe déjà avec ce nom", categorie);
            }
        } else {
            Categories categorieExistante = this.selectById(categorie.getId()).getData();
            categorieExistante.setLibelle(categorie.getLibelle());
            daoCategories.save(categorieExistante);
            return new ServiceResponse<Categories>("2004", "Categorie existe modifiée", categorieExistante);
        }

    }
    }

