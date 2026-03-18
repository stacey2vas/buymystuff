package fr.eni.buymystuff.controllers;

import fr.eni.buymystuff.bo.Adresse;
import fr.eni.buymystuff.bo.Utilisateurs;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthTestController {
    @GetMapping("/register")
    public String showRegisterForm(Model model) {

        Utilisateurs utilisateur = new Utilisateurs();
        utilisateur.setAdresse(new Adresse());

        model.addAttribute("utilisateur", utilisateur);

        return "test/register";
    }

    @PostMapping ("/process-register")
    public String processAuth(@ModelAttribute("utilisateur") Utilisateurs utilisateur, Model model) {
        //Dev en cours
        //TODO : Gérer le process de connexion.
        return null;
    }
}
