package fr.eni.buymystuff.dao;

import fr.eni.buymystuff.DTO.ArticleFormDTO;
import fr.eni.buymystuff.DTO.EnchereDTO;
import fr.eni.buymystuff.bo.Encheres;

import java.io.IOException;
import java.util.List;

public interface IDAOEnchere {
    List<Encheres> selectEncheresByArticleId(Long id);
    void addEnchere(EnchereDTO enchere, Long idUser) throws IOException;

}
