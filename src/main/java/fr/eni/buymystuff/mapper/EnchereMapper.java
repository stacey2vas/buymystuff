package fr.eni.buymystuff.mapper;

import fr.eni.buymystuff.DTO.EnchereDTO;
import fr.eni.buymystuff.bo.Encheres;
import org.springframework.stereotype.Component;

@Component
public class EnchereMapper {

    public EnchereDTO toDTO(Encheres enchere) {
        EnchereDTO dto = new EnchereDTO();
        dto.setId((int) enchere.getId());
        dto.setArticle(enchere.getArticle());
        dto.setDateEnchere(enchere.getDateEnchere());
        dto.setMontantEnchere(enchere.getMontantEnchere());
        return dto;
    }

}