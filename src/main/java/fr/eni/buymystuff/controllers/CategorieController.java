package fr.eni.buymystuff.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
 import org.springframework.web.bind.annotation.PostMapping;
 
 import fr.eni.buymystuff.DTO.ArticleFormDTO;
import fr.eni.buymystuff.bo.Categories;

 

@Controller
public class CategorieController {

    @GetMapping("/add-categorie")
    public String showCategorieForm(Model model ) {
        System.out.println("je passe ici");
        model.addAttribute("categorie", new Categories());  
        return "ajout-categories";
    }

    
    
}
