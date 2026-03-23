package fr.eni.buymystuff.services;

import fr.eni.buymystuff.DTO.ArticleFormDTO;
import fr.eni.buymystuff.DTO.EnchereDTO;
import fr.eni.buymystuff.bo.Articles;
import fr.eni.buymystuff.bo.Encheres;
import fr.eni.buymystuff.bo.Utilisateurs;
import fr.eni.buymystuff.dao.IDAOEnchere;
import fr.eni.buymystuff.mapper.ArticleMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class EnchereService {
    private final IDAOEnchere idaoEnchere;
    private final ArticleService articleService;
    private final ArticleMapper articleMapper;
    public EnchereService(IDAOEnchere idaoEnchere, ArticleService articleService, ArticleMapper articleMapper) {
        this.idaoEnchere = idaoEnchere;
        this.articleService = articleService;
        this.articleMapper = articleMapper;
    }

    public ServiceResponse<List<Encheres>> getAllEncheresByArticleId(Long id ) {
        List<Encheres> encheres = idaoEnchere.selectEncheresByArticleId(id);
        return new ServiceResponse<>("4000", "Article récupéré : ", encheres);
    }
    public ServiceResponse<?> addEnchere(EnchereDTO dto, ArticleFormDTO articleFormDTO, Utilisateurs user) throws IOException {
        Articles article = articleMapper.toEntity(articleFormDTO);
        dto.setArticle(article);
        dto.setUtilisateur(user);

        idaoEnchere.addEnchere(dto); // DAO prend dto complet

        return new ServiceResponse<>("4000", "Succès", dto);
    }
}
