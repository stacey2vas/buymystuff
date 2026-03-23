package fr.eni.buymystuff.controllers;


import fr.eni.buymystuff.DTO.ArticleFormDTO;
import fr.eni.buymystuff.bo.Articles;
import fr.eni.buymystuff.bo.Categories;
import fr.eni.buymystuff.bo.Encheres;
import fr.eni.buymystuff.bo.Utilisateurs;
import fr.eni.buymystuff.services.ArticleService;
import fr.eni.buymystuff.services.AuthService;
import fr.eni.buymystuff.services.EnchereService;
import fr.eni.buymystuff.services.ServicesCategories;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class EnchereController {

    private final ArticleService articleService;
    private final ServicesCategories servicesCategories;
    private final AuthService authService;
    private final EnchereService enchereService;
    public EnchereController(ArticleService articleService, ServicesCategories servicesCategories, AuthService authService, EnchereService enchereService) {
        this.articleService = articleService;
        this.servicesCategories = servicesCategories;
        this.authService = authService;
        this.enchereService = enchereService;
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
    public String showArticle(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetails userDetails, Model model) {
        Utilisateurs user = authService.getUserByPseudo(userDetails.getUsername()).data;
        ArticleFormDTO article = articleService.getArticleDTOById(id).data;
        List<Encheres> encheres = enchereService.getAllEncheresByArticleId(id).data;
        model.addAttribute("article", article);
        model.addAttribute("user",user);
        model.addAttribute("encheres",encheres);
        return "/test/enchere";
    }

}
