package fr.eni.buymystuff.services;

import fr.eni.buymystuff.DTO.EnchereDTO;
import fr.eni.buymystuff.bo.Encheres;
import fr.eni.buymystuff.dao.IDAOFontTest;
import fr.eni.buymystuff.mapper.EnchereMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceFontTest {

    private final IDAOFontTest idaoFontTest;
    private final EnchereMapper enchereMapper;

    public ServiceFontTest(IDAOFontTest idaoFontTest, EnchereMapper enchereMapper) {
        this.idaoFontTest = idaoFontTest;
        this.enchereMapper = enchereMapper;
    }

    public ServiceResponse<List<EnchereDTO>> getAllEncheresByUserId(Long userId) {
        List<Encheres> encheres = idaoFontTest.selectEncheresByUserId(userId);

        if (encheres == null || encheres.isEmpty()) {
            return new ServiceResponse<>("NO_DATA", "Aucune enchère trouvée", List.of());
        }

        List<EnchereDTO> dtos = encheres.stream()
                .map(enchereMapper::toDTO)
                .toList();

        return new ServiceResponse<>("SUCCESS", "Enchères utilisateur récupérées", dtos);
    }
}