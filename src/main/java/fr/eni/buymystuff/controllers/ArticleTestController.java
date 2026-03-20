    package fr.eni.buymystuff.controllers;

    import fr.eni.buymystuff.DTO.ArticleFormDTO;
    import fr.eni.buymystuff.bo.Adresse;
    import fr.eni.buymystuff.bo.Articles;
    import fr.eni.buymystuff.bo.Categories;
    import fr.eni.buymystuff.bo.Utilisateurs;
    import fr.eni.buymystuff.services.ArticleService;
    import fr.eni.buymystuff.services.ServiceResponse;

    import org.springframework.security.core.annotation.AuthenticationPrincipal;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.validation.BindingResult;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.ModelAttribute;
    import org.springframework.web.bind.annotation.PathVariable;
    import org.springframework.web.bind.annotation.PostMapping;

    import java.util.ArrayList;
    import java.util.List;

    @Controller
    public class ArticleTestController {
        private final ArticleService articleService;
        public ArticleTestController(ArticleService articleService) {
            this.articleService = articleService;
        }

        @GetMapping("/add-article")
        public String showAddArticle(Model model) {
            modelFormInformations(model);
            model.addAttribute("articleForm", new ArticleFormDTO());
            return "/test/ajout-article";
        }
        @PostMapping("/add-article")
        public String addArticle(@ModelAttribute("articleForm") ArticleFormDTO articleFormDTO,@AuthenticationPrincipal UserDetails userDetails, Model model) {

            // Vérification de l'image
            ServiceResponse<ArticleFormDTO> imageCheck = articleService.verifyImageInput(articleFormDTO);
            if (!"4000".equals(imageCheck.getCode())) {
                model.addAttribute("errorMessage", imageCheck.getMessage());
                modelFormInformations(model);

                return "/test/ajout-article"; // Réaffiche le formulaire avec les données
            }

            // Si tout est OK, sauvegarder l'article
            ServiceResponse<?> response = articleService.saveArticle(articleFormDTO);
            if ("4000".equals(response.getCode())) {
                return "redirect:/accueil"; // succès
            } else {
                model.addAttribute("errorMessage", response.getMessage());
                modelFormInformations(model);

                return "/test/ajout-article"; // Réaffiche le formulaire en cas d'erreur
            }
        }

        @GetMapping("/modify-article/{id}")
        public String modifyArticle(Model model, @PathVariable Long id) {
            ArticleFormDTO articleFormDTO = articleService.getArticleDTOById(id).data;
            model.addAttribute("articleForm", articleFormDTO);
            modelFormInformations(model);
            return "/test/ajout-article";
        }
        private void modelFormInformations(Model model) {
            List<Categories> categories = new ArrayList<>();

            Categories electronique = new Categories(1L,"Electronique");
            Categories bricolage = new Categories(2L,"Bricolage");
            Adresse adresse = new Adresse(1L, "12 rue des Lilas", "44000", "Nantes");

            categories.add(electronique);
            categories.add(bricolage);

            model.addAttribute("categories", categories);
            model.addAttribute("adresse", adresse);
        }
    }
