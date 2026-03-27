package fr.eni.buymystuff.controllers;

import fr.eni.buymystuff.DTO.EnchereDTO;
import fr.eni.buymystuff.services.AuthService;
import fr.eni.buymystuff.services.ServiceFontTest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class FrontTestController {

    private final AuthService authService;
    private final ServiceFontTest serviceFontTest;

    public FrontTestController(AuthService authService, ServiceFontTest serviceFontTest) {
        this.authService = authService;
        this.serviceFontTest = serviceFontTest;
    }

    @GetMapping("/mes-encheres")
    public String afficherMesEncheres(Model model, @AuthenticationPrincipal UserDetails user) {

        if (user == null) {
            return "redirect:/login";
        }

        var response = authService.getIdUserByPseudo(user.getUsername());

        if (response == null || response.getData() == null) {
            throw new RuntimeException("Utilisateur introuvable");
        }

        long idUser = response.getData();

        List<EnchereDTO> encheres = serviceFontTest
                .getAllEncheresByUserId(idUser)
                .getData();

        model.addAttribute("encheres", encheres);
        model.addAttribute("pseudo", user.getUsername());

        return "mes-encheres";
    }
}
