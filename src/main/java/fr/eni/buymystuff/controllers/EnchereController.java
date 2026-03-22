package fr.eni.buymystuff.controllers;


import fr.eni.buymystuff.DTO.ArticleFormDTO;
import fr.eni.buymystuff.bo.Articles;
import fr.eni.buymystuff.bo.Categories;
import fr.eni.buymystuff.bo.Encheres;
import fr.eni.buymystuff.services.ArticleService;
import fr.eni.buymystuff.services.ServicesCategories;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class EnchereController {

    private final ArticleService articleService;
    private final ServicesCategories servicesCategories;

    public EnchereController(ArticleService articleService, ServicesCategories servicesCategories) {
        this.articleService = articleService;
        this.servicesCategories = servicesCategories;
    }

    @GetMapping("/")
    public String accueil(Model model) {
        List<Articles> articles = articleService.getAllArticles().data;
        List<Categories> categories = servicesCategories.getCategoriesCatalog().data;

        // On ajoute la liste au modèle
        model.addAttribute("articles", articles);
        model.addAttribute("categories", categories);
        // Retour du template Thymeleaf
        return "/test/accueil";
    }
    @GetMapping("/enchere/{id}")
    public String showArticle(@PathVariable("id") Long id, Model model) {
        ArticleFormDTO article = articleService.getArticleDTOById(id).data;
        model.addAttribute("article", article);

        return "/test/enchere";
    }

}
