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

        ServiceResponse<Categories> categorieExist = servicesCategories.getCategoriesByLibelle(categorie.getLibelle());

          if (categorieExist.getData() == null) {
              servicesCategories.saveCategories(categorie);
              return "ajout-categories";
          } else {
              model.addAttribute("erreur message", "Erreur lors de l'ajout !");
              return "ajout-categories";
          }
    }
}
