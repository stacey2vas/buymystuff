package fr.eni.buymystuff.controllers;

import java.util.List;

import fr.eni.buymystuff.bo.Utilisateurs;
import fr.eni.buymystuff.services.AuthService;
import fr.eni.buymystuff.services.ServiceResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.eni.buymystuff.DTO.ArticleFilterDTO;
import fr.eni.buymystuff.DTO.ArticleFormDTO;
import fr.eni.buymystuff.services.ArticleService;

@Controller
@RequestMapping("/api")
public class APIController {
    private final ArticleService articleService;
    private final AuthService authService;

    public APIController(ArticleService articleService, AuthService authService) {
        this.articleService = articleService;
        this.authService = authService;
    }

    @PostMapping("/articles")
    public String getArticles(@AuthenticationPrincipal UserDetails userDetails, @RequestBody ArticleFilterDTO filter,  Model model) {
        try {
            if (userDetails != null) { // Savoir si le user est connecté
                Utilisateurs user = authService.getUserByPseudo(userDetails.getUsername()).data;
                Long idUser = (long) user.getId();
                model.addAttribute("user", user);
                    List<ArticleFormDTO> articles = articleService.getArticlesByFilter(filter, idUser).data;
                    model.addAttribute("articles", articles);
            } else {
                List<ArticleFormDTO> articles = articleService.getArticlesByFilter(filter, null).data;
                model.addAttribute("articles", articles);
            }
            return "fragments/cardArticle :: cardArticle";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", e.getMessage());
            return "fragments/cardArticle :: cardArticle";
        }
    }
    @GetMapping("/creditUser")
    public ResponseEntity<Integer> getCreditUser(@AuthenticationPrincipal UserDetails userDetails) {
        System.out.println("je passe ici");
        Utilisateurs user = authService.getUserByPseudo(userDetails.getUsername()).data;
        return ResponseEntity.ok(user.getCredit());
        // ResponseEntity est une classe de Spring qui te permet de contrôler complètement la réponse HTTP.
        //👉 Avec ça tu peux gérer :
        //le body (les données envoyées)
        //le status HTTP (200, 404, 401…)
        //les headers
    }
    @PostMapping("/delete-account")
    public void getArticles(@AuthenticationPrincipal UserDetails userDetails) {
        Utilisateurs user = authService.getUserByPseudo(userDetails.getUsername()).data;
        ServiceResponse<?> response = authService.deleteAccount(user);

    }
}
