package fr.eni.buymystuff.controllers;

import fr.eni.buymystuff.bo.Categories;
import fr.eni.buymystuff.services.ServiceResponse;
import fr.eni.buymystuff.services.ServicesCategories;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class EnchereController {

    private final ServicesCategories servicesCategories;

    public EnchereController(ServicesCategories servicesCategories) {
        this.servicesCategories = servicesCategories;
    }

    @GetMapping("/ajout-categories")
    public String ajout(Model model) {

        Categories categorie = new Categories();

        model.addAttribute("categorie", categorie);

        return "ajout-categories";
    }

    @PostMapping("/ajout-categories")
    public String ajoutProcess(@ModelAttribute("categorie") Categories categorie, Model model) {

        ServiceResponse<Categories> categorieExist =
                servicesCategories.getCategoriesByLibelle(categorie.getLibelle());

        if (categorieExist != null && categorieExist.getData() != null) {
            model.addAttribute("message", "La catégorie existe déjà !");
            return "ajout-categories";
        }

        ServiceResponse<Categories> serviceResponse =
                servicesCategories.saveCategories(categorie);

        if (serviceResponse == null || !"2000".equals(serviceResponse.getCode())) {
            model.addAttribute("message", "Erreur lors de l'ajout !");
            return "ajout-categories";
        }

        model.addAttribute("message", "Catégorie ajoutée avec succès !");
        return "ajout-categories";
    }
}

