package fr.eni.buymystuff.dao;

import fr.eni.buymystuff.DTO.ArticleFormDTO;
import fr.eni.buymystuff.bo.Articles;
import fr.eni.buymystuff.bo.Categories;

import java.io.IOException;
import java.util.List;

public interface  IDAOArticle {
    void saveArticle(ArticleFormDTO article) throws IOException;
    Articles findArticleById(Long id);
    List<Articles> findAllArticlesByUserId(int id);
    List<Categories> getAllCategories();
}
