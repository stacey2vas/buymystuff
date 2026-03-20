package fr.eni.buymystuff.mapper;

import fr.eni.buymystuff.DTO.ArticleFormDTO;
import fr.eni.buymystuff.bo.Adresse;
import fr.eni.buymystuff.bo.Articles;
import fr.eni.buymystuff.bo.Categories;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ArticleMapper {

    public ArticleFormDTO toFormDTO(Articles article ) {

        if (article == null) {
            return null;
        }
        ArticleFormDTO dto = new ArticleFormDTO();
        dto.setId(article.getId());
        dto.setNomArticle(article.getNomArticle());
        dto.setDescription(article.getDescription());
        dto.setDateDebut(article.getDateDebut());
        dto.setDateFin(article.getDateFin());
        dto.setPrixInitial(article.getPrixInitial());
        dto.setCategories(article.getCategories());
        dto.setAdresseProprietaire(article.getAdresseProprietaire());

        return dto;
    }

    public Articles toEntity(ArticleFormDTO dto) {

        if (dto == null) {
            return null;
        }

       Articles article = new Articles();

        article.setId(dto.getId());
        article.setNomArticle(dto.getNomArticle());
        article.setDescription(dto.getDescription());
        article.setDateDebut(dto.getDateDebut());
        article.setDateFin(dto.getDateFin());
        article.setPrixInitial(dto.getPrixInitial());
        article.setCategories(dto.getCategories());
        article.setAdresseProprietaire(dto.getAdresseProprietaire());

        return article;
    }
    public void updateEntityFromFormDTO(ArticleFormDTO dto, Articles article) {


    }
}