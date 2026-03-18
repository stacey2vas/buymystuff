package fr.eni.buymystuff.services;


import fr.eni.buymystuff.DTO.ArticleFormDTO;
import fr.eni.buymystuff.bo.Articles;
import fr.eni.buymystuff.bo.Categories;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ArticleService {

    public ServiceResponse<ArticleFormDTO> verifyImageInput(ArticleFormDTO articleFormDTO) {
        if (articleFormDTO.getId() == null && (articleFormDTO.getImageFile() == null || articleFormDTO.getImageFile().isEmpty())) {
            return new ServiceResponse<>("4001", "L'image est obligatoire pour créer un film", articleFormDTO);
        }
        return new ServiceResponse<ArticleFormDTO>("4000", "OK", articleFormDTO);
    }
    public ServiceResponse<?>saveArticle(ArticleFormDTO articleFormDTO){

        return new ServiceResponse<ArticleFormDTO>("4000", "OK", articleFormDTO);
    }
    public ServiceResponse<Categories> findCategorieById(Long id) {
        if(id == 1){
            Categories categorie = new Categories(1,"Electronique");
            return new ServiceResponse<>("4000", "Movie ajouté : ", categorie);
        }else{
            Categories categorie = new Categories(2,"Bricolage");
            return new ServiceResponse<>("4000", "Movie ajouté : ", categorie);
        }


    }
}
