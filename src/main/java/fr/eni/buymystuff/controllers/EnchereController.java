package fr.eni.buymystuff.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EnchereController {

    public EnchereController() {
    }

    @GetMapping("/")
    public String accueil(){

        return "accueil";
    }
}
