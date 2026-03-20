package fr.eni.buymystuff.controllers;

import fr.eni.buymystuff.bo.Categories;
import fr.eni.buymystuff.services.ServiceResponse;
import fr.eni.buymystuff.services.ServicesCategories;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class EnchereController {

    private final ServicesCategories servicesCategories;

    public EnchereController(ServicesCategories servicesCategories) {
        this.servicesCategories = servicesCategories;
    }

    @GetMapping("/ajout-categories")
    public String showForm(Model model) {
        model.addAttribute("categorie", new Categories());
        return "ajout-categories";
    }

    @GetMapping("/modifier-categorie/{id}")
    public String findById(@PathVariable Long id, Model model) {

        ServiceResponse<Categories> response = servicesCategories.selectById(id);
        // IF pour savoir si le select renvoie une catégorie ou null
        // Si c'est null model attribute message ... avec une div pour le message dans le html
        model.addAttribute("categorie", response.getData());

        return "ajout-categories";
    }

    @PostMapping("/ajout-categories")
    public String save(@ModelAttribute Categories categorie, Model model) {

        ServiceResponse<Categories> response = servicesCategories.saveCategories(categorie);
        model.addAttribute("categorie", categorie);
        if(response.getCode().equals("2005")){
            model.addAttribute("errorMessage", response.getMessage());
        }
        return "redirect:/ajout-categories";
    }

}