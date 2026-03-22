package fr.eni.buymystuff.controllers;


import fr.eni.buymystuff.bo.Articles;
import fr.eni.buymystuff.bo.Encheres;
import fr.eni.buymystuff.services.ArticleService;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class EnchereController {

    private final ArticleService articleService;

    public EnchereController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/")
    public String accueil(Model model) {
        List<Articles> articles = articleService.getAllArticles().data;
        // On ajoute la liste au modèle
        model.addAttribute("articles", articles);
        // Retour du template Thymeleaf
        return "/test/accueil";
    }
    @GetMapping("/enchere/{id}")
    public String showArticle(@PathVariable("id") Long id, Model model) {
        return "/test/enchere";
    }

    @GetMapping("/enchere")
    public String enchere(Model model) {
        model.addAttribute("enchere", new Encheres());
        return "test/enchere";
    }
}
