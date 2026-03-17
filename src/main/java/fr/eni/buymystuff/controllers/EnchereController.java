package fr.eni.buymystuff.controllers;

import fr.eni.buymystuff.bo.Categories;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class EnchereController {

    public EnchereController() {
    }

    @GetMapping("/ajout-categories")
    public String ajout(Model model) {

        Categories categorie = new Categories();

        model.addAttribute("categorie", categorie);

        return "ajout-categories";
    }

    @PostMapping("/ajout-categories")
    public String ajoutProcess(@ModelAttribute("categorie") Categories categorie, Model model) {

        model.addAttribute("message", "ajoutée avec succés");
        return "ajout-categories";
    }
}
