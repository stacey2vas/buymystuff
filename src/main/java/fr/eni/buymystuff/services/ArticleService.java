package fr.eni.buymystuff.services;


import fr.eni.buymystuff.DTO.ArticleFilterDTO;
import fr.eni.buymystuff.DTO.ArticleFormDTO;
import fr.eni.buymystuff.bo.Articles;
import fr.eni.buymystuff.dao.IDAOArticle;

import fr.eni.buymystuff.mapper.ArticleMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

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

    public ServiceResponse<List<Articles>> getAllArticles( ) {
        List<Articles> articles = idaoArticle.getAllArticles();
        return new ServiceResponse<>("4000", "Article récupéré : ", articles);
    }

    public ServiceResponse<?> saveArticle(ArticleFormDTO dto, Long idUser) {
        try {
            idaoArticle.saveArticle(dto, idUser);
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

    public ServiceResponse<List<ArticleFormDTO>> getArticlesByFilter(ArticleFilterDTO filter) {
       // Normaliser les valeurs vides en null
        String nomArticle = (filter.getNomArticle() == null || filter.getNomArticle().isBlank()) ? null : filter.getNomArticle();
        String categorie = (filter.getCategorie() == null || filter.getCategorie().isBlank()) ? null : filter.getCategorie();
        Integer prixMin = filter.getPrixMin();
        Integer prixMax = filter.getPrixMax();
        // Dates
       LocalDateTime dateStart = (filter.getDateStart() != null) 
        ? filter.getDateStart().atStartOfDay()  // 00:00:00
        : null;

        LocalDateTime dateEnd = (filter.getDateEnd() != null) 
        ? filter.getDateEnd().atTime(23, 59, 59)  // 23:59:59
        : null;
        List<ArticleFormDTO> articles = idaoArticle.findBySearch(nomArticle, categorie, prixMin, prixMax, dateStart, dateEnd);

        return new ServiceResponse<>("4000", "Film trouvé", articles);
    }

}


