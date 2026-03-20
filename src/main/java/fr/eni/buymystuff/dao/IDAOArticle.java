package fr.eni.buymystuff.dao;

import fr.eni.buymystuff.DTO.ArticleFormDTO;
import fr.eni.buymystuff.bo.Articles;

import java.util.List;

public interface  IDAOArticle {
    void saveArticle(ArticleFormDTO article);
    Articles findArticleById(Long id);
    List<Articles> findAllArticlesByUserId(int id);
}
