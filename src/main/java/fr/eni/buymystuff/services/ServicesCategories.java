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
        return ServiceResponse.buildResponse("202","Categorie récupéré avec succès", categories);
    }

    public ServiceResponse <Categories> getCategoriesByLibelle(String libelle) {
        Categories categorie = daoCategories.selectByLibelle(libelle);
        if(categorie == null){
            // Si il ne l'a pas trouvé on return null
            return new ServiceResponse<>("2005", "Categorie n'existe pas", null);
        } else {
            // Si il l'a trouvé on return la catégorie
            return new ServiceResponse<>("2005", "Categorie bien récupérée", categorie);
        }
    }

    public ServiceResponse <Categories> selectById(Long id) {

        Categories categorie = daoCategories.selectById(id);
        // IF pour savoir si c'est null tu fais un un new service response avec son code , mess et null
        // Si il existe celui du dessous
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
            // Création
            ServiceResponse<Categories> categorieExist = this.getCategoriesByLibelle(categorie.getLibelle());
            if (categorieExist.getData() == null) {
                System.out.println("je passe ici" );
                daoCategories.save(categorie);
                return new ServiceResponse<Categories>("2004", "Categorie créée avec succès", categorie);
            } else {
                return new ServiceResponse<Categories>("2005", "Categorie existe déjà avec ce nom", categorie);
            }
        } else {
            // Update
            Categories categorieExistante = this.selectById(categorie.getId()).getData();
            categorieExistante.setLibelle(categorie.getLibelle());
            daoCategories.save(categorieExistante);
            return new ServiceResponse<Categories>("2004", "Categorie existe modifiée", categorieExistante);
        }

    }
    }

