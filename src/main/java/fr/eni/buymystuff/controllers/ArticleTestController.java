package fr.eni.buymystuff.controllers;

import fr.eni.buymystuff.bo.Articles;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ArticleTestController {

    @GetMapping("/add-article")
    public String showAddArticle(Model model) {
        model.addAttribute("articleForm", new Articles());

        return "/test/ajout-article";
    }
    // Traiter le formulaire d'ajout de film, en validant les données et en créant ou modifiant le film dans la base de données
    @PostMapping("/admin/save-movie")
    public String addMovie(@ModelAttribute("articleForm") Articles article, Model model) {


        // Si binding ou image KO
//        if (bindingResult.hasErrors() || !"4000".equals(imageCheck.getCode())) {
//            populateFormLists(model);
//
//            // Afficher message erreur image
//            if (!"4000".equals(imageCheck.getCode())) {
//                model.addAttribute("errorMessage", imageCheck.getMessage());
//            }
//            return "add-movie";
//        }
//        // Tout est OK → on peut sauver le film
//        ResponseService<?> response = movieService.saveMovie(movieForm);
//        if(response.getCode().equals("4000")){
//            return "redirect:/"; // succès
//        } else {
//            return "add-movie";
//        }
        return "add-article";
    }
}
