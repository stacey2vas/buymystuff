package fr.eni.buymystuff.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import fr.eni.buymystuff.bo.Articles;
import fr.eni.buymystuff.services.ArticleService;
import fr.eni.buymystuff.services.ServiceResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class FrontTestController {
    private final ArticleService articleService;
    
    public FrontTestController(ArticleService  articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/login")
    public String login(Model model){

        return "/test/login";
    }
    
    

   
}
