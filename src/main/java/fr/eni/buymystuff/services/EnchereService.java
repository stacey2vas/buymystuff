package fr.eni.buymystuff.services;

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
}
