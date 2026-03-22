package fr.eni.buymystuff.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    
        public APIController(ArticleService articleService) {
            this.articleService = articleService;
        }

        @PostMapping("/articles")
    public String getArticles(@RequestBody ArticleFilterDTO filter, Model model) {
        try {
            // récupère les articles filtrés
            List<ArticleFormDTO> articles = articleService.getArticlesByFilter(filter).data;
            model.addAttribute("articles", articles);
            return "fragments/cardArticle :: cardArticle"; // ton fragment
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", e.getMessage());
            return "fragments/cardArticle :: cardArticle";
        }
    }
    
}
