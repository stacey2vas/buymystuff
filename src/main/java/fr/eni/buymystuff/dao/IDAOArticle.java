package fr.eni.buymystuff.dao;

import fr.eni.buymystuff.DTO.ArticleFormDTO;
import fr.eni.buymystuff.bo.Articles;
import fr.eni.buymystuff.bo.Categories;
import fr.eni.buymystuff.bo.Utilisateurs;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public interface  IDAOArticle {
    void saveArticle(ArticleFormDTO article, Long idUser) throws IOException;
    Articles findArticleById(Long id);
    List<Articles> findAllArticlesByUserId(int id);
    List<Articles> getAllArticles();
    List<ArticleFormDTO> findBySearch(String nomArticle, String categorie,
                                      Integer prixMin, Integer prixMax,
                                      String statut,String selectValue, Long idUser);
    Utilisateurs selectUserById(Long id);
}
