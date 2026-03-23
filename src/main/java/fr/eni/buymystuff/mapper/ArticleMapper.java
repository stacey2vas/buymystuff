package fr.eni.buymystuff.mapper;

import fr.eni.buymystuff.DTO.ArticleFormDTO;
import fr.eni.buymystuff.bo.Adresse;
import fr.eni.buymystuff.bo.Articles;
import fr.eni.buymystuff.bo.Categories;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

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

        Adresse adresseEntity = article.getAdresseProprietaire();

        if (adresseEntity != null) {
            String adresseString = adresseEntity.getRue()
                    + ", " + adresseEntity.getCodePostal()
                    + ", " + adresseEntity.getVille();
            dto.setAdresseString(adresseString);
        } else {
            dto.setAdresseString("");
        }
        dto.setPrixVente(article.getPrixVente());
        dto.setAdresseProprietaire(adresseEntity);
        dto.setImage(article.getImage());
        dto.setCategoriesIds(
                article.getCategories()
                        .stream()               // parcourt chaque Categorie
                        .map(Categories::getId)  // prend seulement l'ID
                        .collect(Collectors.toList()) // transforme en List<Long>
        );
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