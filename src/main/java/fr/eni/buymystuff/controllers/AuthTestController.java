package fr.eni.buymystuff.controllers;

import fr.eni.buymystuff.bo.Adresse;
import fr.eni.buymystuff.bo.Utilisateurs;
import fr.eni.buymystuff.services.AuthService;
import fr.eni.buymystuff.services.ServiceResponse;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthTestController
{
    private final AuthService authService;

    public AuthTestController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String login(Model model){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "user";  // ton mot de passe en clair
        String rawPassword2 = "admin";  // ton mot de passe en clair
        String rawPassword3 = "test";  // ton mot de passe en clair
        String encodedPassword = encoder.encode(rawPassword);
        String encodedPassword2 = encoder.encode(rawPassword2);
        String encodedPassword3 = encoder.encode(rawPassword3);
        System.out.println("user mdp : " + encodedPassword);
        System.out.println("admin mdp : " + encodedPassword2);
        System.out.println("test mdp : " + encodedPassword3);
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {

            model.addAttribute("utilisateur", new Utilisateurs());
            model.addAttribute("adresse", new Adresse());

        return "register";
    }

    @PostMapping ("/process-register")
    public String processAuth(@ModelAttribute("utilisateur") Utilisateurs utilisateur, Model model) {
        // On ajoute l'utilisateur et l'adresse reçue dans la bdd
        ServiceResponse<Utilisateurs> response = authService.ajouterUtilisateur(utilisateur);
        return "register";
    }
 // Route GET pour afficher le profil
    @GetMapping("/profile")
    public String showProfil(@AuthenticationPrincipal UserDetails userDetails, Model model) {
       Utilisateurs user = authService.getUserByPseudo(userDetails.getUsername()).data;
        model.addAttribute("user", user);

        return "profile";
    }

    // Route GET pour modifier le profil
    @GetMapping("/update")
    public String showUpdateProfilForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Utilisateurs user = authService.getUserByPseudo(userDetails.getUsername()).data;
        model.addAttribute("user", user);

        return "update-profile";
    }

    @PostMapping ("/process-update")
    public String processUdpdate(@ModelAttribute("user") Utilisateurs user, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        // On ajoute l'utilisateur et l'adresse reçue dans la bdd
         int idUtil = authService.getIdUserByPseudo(userDetails.getUsername()).data;
         user.setId(idUtil);
        ServiceResponse<Utilisateurs> response = authService.save(user);
        return "profile";
    }
}
