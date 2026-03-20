package fr.eni.buymystuff.controllers;


import fr.eni.buymystuff.bo.Encheres;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EnchereController {

    @GetMapping("/enchere")
    public String enchere(Model model) {
        model.addAttribute("enchere", new Encheres());
        return "test/enchere";
    }
}
