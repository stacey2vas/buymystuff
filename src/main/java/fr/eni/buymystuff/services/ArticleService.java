package fr.eni.buymystuff.services;


import fr.eni.buymystuff.DTO.ArticleFormDTO;
import fr.eni.buymystuff.bo.Articles;
import fr.eni.buymystuff.bo.Categories;
import fr.eni.buymystuff.dao.IDAOArticle;

import fr.eni.buymystuff.mapper.ArticleMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Component
public class ArticleService {

    private final IDAOArticle idaoArticle;
    private final ArticleMapper articleMapper;
    public ArticleService(IDAOArticle idaoArticle, ArticleMapper articleMapper) {
        this.idaoArticle = idaoArticle;
        this.articleMapper = articleMapper;
    }

    public ServiceResponse<ArticleFormDTO> verifyImageInput(ArticleFormDTO articleFormDTO) {
        if (articleFormDTO.getId() == null && (articleFormDTO.getImageFile() == null || articleFormDTO.getImageFile().isEmpty())) {
            return new ServiceResponse<>("4001", "L'image est obligatoire pour créer un film", articleFormDTO);
        }
        return new ServiceResponse<ArticleFormDTO>("4000", "OK", articleFormDTO);
    }

    public ServiceResponse<Categories> findCategorieById(Long id) {
        if(id == 1){
            Categories categorie = new Categories(1L,"Electronique");
            return new ServiceResponse<>("4000", "Movie ajouté : ", categorie);
        }else{
            Categories categorie = new Categories(2L,"Bricolage");
            return new ServiceResponse<>("4000", "Movie ajouté : ", categorie);
        }
    }
    public ServiceResponse<List<Categories>> getAllCategories( ) {
        List<Categories> categories = idaoArticle.getAllCategories();
        return new ServiceResponse<>("4000", "Movie ajouté : ", categories);
    }

    public ServiceResponse<?> saveArticle(ArticleFormDTO dto) {
        try {
            idaoArticle.saveArticle(dto);
            return new ServiceResponse<>("4000", "Succès", dto);
        } catch (Exception e) {
            return new ServiceResponse<>("5000", "Erreur BDD", dto);
        }
    }


    public ServiceResponse<ArticleFormDTO> getArticleDTOById(Long id) {
        Articles article = idaoArticle.findArticleById(id);
        ArticleFormDTO articleFormDTO = articleMapper.toFormDTO(article);

        return new ServiceResponse<>("4000", "Film trouvé", articleFormDTO);
    }
}
