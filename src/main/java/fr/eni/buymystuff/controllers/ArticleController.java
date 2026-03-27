package fr.eni.buymystuff.controllers;

import fr.eni.buymystuff.DTO.ArticleFormDTO;
import fr.eni.buymystuff.bo.Adresse;
import fr.eni.buymystuff.bo.Articles;
import fr.eni.buymystuff.bo.Categories;
import fr.eni.buymystuff.bo.Utilisateurs;
import fr.eni.buymystuff.services.ArticleService;
import fr.eni.buymystuff.services.AuthService;
import fr.eni.buymystuff.services.ServiceResponse;
import fr.eni.buymystuff.services.ServicesCategories;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class ArticleController {
    private final ArticleService articleService;
    private final ServicesCategories servicesCategories;
    private final AuthService authService;
    
    public ArticleController(ArticleService articleService, ServicesCategories servicesCategories, AuthService authService) {
        this.articleService = articleService;
        this.servicesCategories = servicesCategories;
        this.authService = authService;
    }

    @GetMapping("/add-article")
    public String showAddArticle(Model model) {
        modelFormInformations(model);
        model.addAttribute("articleForm", new ArticleFormDTO());
        return "ajout-article";
    }
    @PostMapping("/add-article")
    public String addArticle(@ModelAttribute("articleForm") ArticleFormDTO articleFormDTO,@AuthenticationPrincipal UserDetails userDetails, Model model) {

        int idUser = authService.getIdUserByPseudo(userDetails.getUsername()).data;
        long idUserLong = idUser;
        // Vérification de l'image
        ServiceResponse<ArticleFormDTO> imageCheck = articleService.verifyImageInput(articleFormDTO);
        if (!"4000".equals(imageCheck.getCode())) {
            model.addAttribute("errorMessage", imageCheck.getMessage());
            modelFormInformations(model);

            return "ajout-article"; // Réaffiche le formulaire avec les données
        }
        // Si tout est OK, sauvegarder l'article
        ServiceResponse<?> response = articleService.saveArticle(articleFormDTO, idUserLong);
        if ("4000".equals(response.getCode())) {
            return "redirect:/"; // succès
        } else {
            model.addAttribute("errorMessage", response.getMessage());
            modelFormInformations(model);

            return "ajout-article"; // Réaffiche le formulaire en cas d'erreur
        }
    }

    @GetMapping("/modify-article/{id}")
    public String modifyArticle(Model model, @PathVariable("id") Long id) {
        ArticleFormDTO articleFormDTO = articleService.getArticleDTOById(id).data;

        model.addAttribute("articleForm", articleFormDTO);
        System.out.println("articleFormDTO : " + articleFormDTO.getCategories());
        System.out.println("articleFormDTO : " + articleFormDTO.getCategoriesIds());

        modelFormInformations(model);
        return "ajout-article";
    }
    private void modelFormInformations(Model model) {
        List<Categories> categories = servicesCategories.getCategoriesCatalog().data;
        model.addAttribute("categories", categories);
    }
}
