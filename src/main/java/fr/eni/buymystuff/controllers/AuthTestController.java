package fr.eni.buymystuff.controllers;

import fr.eni.buymystuff.bo.Adresse;
import fr.eni.buymystuff.bo.Utilisateurs;
import fr.eni.buymystuff.services.AuthService;
import fr.eni.buymystuff.services.ServiceResponse;
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


    @GetMapping("/register")
    public String showRegisterForm(Model model) {

            model.addAttribute("utilisateur", new Utilisateurs());
            model.addAttribute("adresse", new Adresse());

        return "test/register";
    }

    @PostMapping ("/process-register")
    public String processAuth(@ModelAttribute("utilisateur") Utilisateurs utilisateur, Model model) {
        // On ajoute l'utilisateur et l'adresse reçue dans la bdd
        ServiceResponse<Utilisateurs> response = authService.ajouterUtilisateur(utilisateur);
        return "test/register";
    }

    // Route GET de test
    @GetMapping("/formulaire-test")
    public String showTestForm() {
       
        return "test/formulaire-test";
    }

    // Route post de test
    @PostMapping("/process-register-test")
    public String processFormulaireTest(@ModelAttribute("utilisateur") Utilisateurs utilisateurTestisateur,  @ModelAttribute("adresse") Adresse adresse, Model model) {
        Adresse adresseTest = new Adresse();
        Utilisateurs utilisateurTest = new Utilisateurs();
        // Adresse
        adresseTest.setRue("82 rue de la paix");
        adresseTest.setCodePostal("75000");
        adresseTest.setVille("Paris");

        // utilisateur
        utilisateurTest.setNom("Terrieur3");
        utilisateurTest.setPrenom("Alain3");
        utilisateurTest.setPseudo("toto5"); // Le pseudo est unique donc pas de doublon en BDD
        utilisateurTest.setEmail("aterrieur@eni.fr");
        utilisateurTest.setTelephone("0606060606");
        utilisateurTest.setMotDePasse("toto");
        utilisateurTest.setAdministrateur(false);
        utilisateurTest.setCredit(100);
        utilisateurTest.setActif(true);
        utilisateurTest.setAdresse(adresseTest);

        // Insertion Adresse + User
//        authService.ajouterUtilisateur( utilisateurTest ,adresseTest);

        return "test/formulaire-test";
    }


}
