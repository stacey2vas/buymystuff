package fr.eni.buymystuff.services;

import fr.eni.buymystuff.DTO.ArticleFormDTO;
import fr.eni.buymystuff.DTO.EnchereDTO;
import fr.eni.buymystuff.bo.Articles;
import fr.eni.buymystuff.bo.Encheres;
import fr.eni.buymystuff.dao.IDAOEnchere;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EnchereService {
    private final IDAOEnchere idaoEnchere;

    public EnchereService(IDAOEnchere idaoEnchere) {
        this.idaoEnchere = idaoEnchere;
    }

    public ServiceResponse<List<Encheres>> getAllEncheresByArticleId(Long id ) {
        List<Encheres> encheres = idaoEnchere.selectEncheresByArticleId(id);
        return new ServiceResponse<>("4000", "Article récupéré : ", encheres);
    }
    public ServiceResponse<?> addEnchere(EnchereDTO dto, Long idUser) {
        try {
            idaoEnchere.addEnchere(dto, idUser);
            return new ServiceResponse<>("4000", "Succès", dto);
        } catch (Exception e) {
            return new ServiceResponse<>("5000", "Erreur BDD", dto);
        }
    }
}
