package fr.eni.buymystuff.services;


import fr.eni.buymystuff.DTO.ArticleFilterDTO;
import fr.eni.buymystuff.DTO.ArticleFormDTO;
import fr.eni.buymystuff.bo.Articles;
import fr.eni.buymystuff.bo.Utilisateurs;
import fr.eni.buymystuff.dao.IDAOArticle;

import fr.eni.buymystuff.mapper.ArticleMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ArticleService {

    private final IDAOArticle idaoArticle;
    private final ArticleMapper articleMapper;
    private final AuthService authService;

    public ArticleService(IDAOArticle idaoArticle, ArticleMapper articleMapper, AuthService authService) {
        this.idaoArticle = idaoArticle;
        this.articleMapper = articleMapper;
        this.authService = authService;
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

    public ServiceResponse<List<ArticleFormDTO>> getArticlesByFilter(ArticleFilterDTO filter, Long id) {
        String nomArticle = (filter.getNomArticle() == null || filter.getNomArticle().isBlank()) ? null : filter.getNomArticle();
        String categorie = (filter.getCategorie() == null || filter.getCategorie().isBlank()) ? null : filter.getCategorie();
        Integer prixMin = filter.getPrixMin();
        Integer prixMax = filter.getPrixMax();
        String statut = (filter.getStatut() == null ) ? null : filter.getStatut();
        String selectValue =  (filter.getSelectValue() == null ) ? null : filter.getSelectValue();

        List<ArticleFormDTO> articles = idaoArticle.findBySearch(nomArticle, categorie, prixMin, prixMax, statut, selectValue, id);
        return new ServiceResponse<>("4000", "Articles trouvés", articles);
    }
    public ServiceResponse<Utilisateurs> getUserById(Long id) {
        Utilisateurs user = idaoArticle.selectUserById(id);
        return new ServiceResponse<Utilisateurs>("4000", "Film trouvé", user);
    }
}


