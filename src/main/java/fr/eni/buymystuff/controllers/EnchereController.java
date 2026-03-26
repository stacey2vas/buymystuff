package fr.eni.buymystuff.controllers;


import fr.eni.buymystuff.DTO.ArticleFormDTO;
import fr.eni.buymystuff.DTO.EnchereDTO;
import fr.eni.buymystuff.bo.Articles;
import fr.eni.buymystuff.bo.Categories;
import fr.eni.buymystuff.bo.Encheres;
import fr.eni.buymystuff.bo.Utilisateurs;
import fr.eni.buymystuff.services.*;

import java.io.IOException;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String accueil(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if(userDetails != null){
        Utilisateurs user = authService.getUserByPseudo(userDetails.getUsername()).data;
        if (user != null) {
            model.addAttribute("userCredit", user.getCredit());
            model.addAttribute("user", user);
        }
        }
        List<Articles> articles = articleService.getAllArticles().data;
        List<Categories> categories = servicesCategories.getCategoriesCatalog().data;

        // On ajoute la liste au modèle
        model.addAttribute("articles", articles);
        model.addAttribute("categories", categories);
        // Retour du template Thymeleaf
        return "/test/accueil";
    }
    @GetMapping("/enchere/{id}")
    public String showArticle(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetails userDetails, EnchereDTO enchereDTO, Model model) {
        Utilisateurs user = authService.getUserByPseudo(userDetails.getUsername()).data;
        ArticleFormDTO article = articleService.getArticleDTOById(id).data;
        List<Encheres> encheres = enchereService.getAllEncheresByArticleId(id).data;
        model.addAttribute("article", article);
        model.addAttribute("user",user);
        model.addAttribute("encheres",encheres);
        model.addAttribute("enchereForm",new EnchereDTO());
        return "/test/enchere";
    }
    @PostMapping("/add-enchere/{id}")
    public String addEnchere(@PathVariable("id") Long id,
                             @AuthenticationPrincipal UserDetails userDetails,
                             @ModelAttribute("enchereForm") EnchereDTO enchereDTO) throws IOException {

        Utilisateurs user = authService.getUserByPseudo(userDetails.getUsername()).data;
        ArticleFormDTO articleDTO = articleService.getArticleDTOById(id).data;

        enchereService.addEnchere(enchereDTO, articleDTO, user);

        return "redirect:/enchere/" + id;
    }
    @PostMapping("/add-enchere-modal")
    public String addEnchereModal(@AuthenticationPrincipal UserDetails userDetails,
                                  @RequestParam("articleId") Long articleId,
                                  @RequestParam("montantEnchere") Integer montantEnchere) throws IOException {

        Utilisateurs user = authService.getUserByPseudo(userDetails.getUsername()).data;
        ArticleFormDTO articleDTO = articleService.getArticleDTOById(articleId).data;

        EnchereDTO enchereDTO = new EnchereDTO();
        enchereDTO.setMontantEnchere(montantEnchere);

        enchereService.addEnchere(enchereDTO, articleDTO, user);

        return "redirect:/enchere/" + articleId; // ou JSON si tu veux faire un fetch/AJAX
    }
}
