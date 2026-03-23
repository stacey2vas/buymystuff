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

import java.util.List;

@Controller
public class CategoriesController {
    private final ServicesCategories servicesCategories;

    public CategoriesController(ServicesCategories servicesCategories) {
        this.servicesCategories = servicesCategories;
    }

    @GetMapping("/add-categorie")
    public String showForm(Model model) {
        List<Categories> categories = servicesCategories.getCategoriesCatalog().data;

        model.addAttribute("categorie", new Categories());
        model.addAttribute("categories", categories);

        return "ajout-categories";
    }

    @GetMapping("/modifier-categorie/{id}")
    public String findById(@PathVariable Long id, Model model) {

        ServiceResponse<Categories> response = servicesCategories.selectById(id);
        if (response.getData() != null) {
            model.addAttribute("categorie", response.getData());
        } else {
            model.addAttribute("message", "La catégorie avec l'ID " + id + " n'existe pas.");
            model.addAttribute("categorie", new Categories());
        }
        return "ajout-categories";
    }

    @PostMapping("/ajout-categories")
    public String save(@ModelAttribute Categories categorie, Model model) {

        ServiceResponse<Categories> response = servicesCategories.saveCategories(categorie);
        if(response.getCode().equals("2005")){
            model.addAttribute("categorie", categorie);
            model.addAttribute("message", response.getMessage());
            return "ajout-categories";
        }
        else {
            model.addAttribute("message", response.getMessage());
            model.addAttribute("categorie", new Categories());
            return "ajout-categories";
        }
    }
}