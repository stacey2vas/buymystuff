package fr.eni.buymystuff.controllers;

import fr.eni.buymystuff.bo.Utilisateurs;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthTestController {
    @GetMapping("/show-auth-form")
    public String showAuthForm(Model model) {
        // Instancier un utilisateur par defaut dans le formulaire
        Utilisateurs utilisateur = new Utilisateurs();

        model.addAttribute("utilisateur", utilisateur);

        return "test/login-auth-page";
    }

    @PostMapping ("/process-auth")
    public String processAuth(@ModelAttribute("utilisateur") Utilisateurs utilisateur, Model model) {
        //Dev en cours
        //TODO : Gérer le process de connexion.
        return null;
    }
}
