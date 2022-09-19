package com.application.dungeonsthymeleaf;

import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class CharacterController {
    public List<PlayableCharacter> charactersList;

    public CharacterController() {
        this.charactersList = new ArrayList<>();
        charactersList.add(0, new PlayableCharacter(1, "Loris", "Magicien", 12));
        charactersList.add(1, new PlayableCharacter(2, "Allan", "Prêtre", 8));
        charactersList.add(2, new PlayableCharacter(3, "Jipékof", "Démoniste", 20));
    }

    @RequestMapping("/characters")
    public String getTemplate(Model model) {
        model.addAttribute("characters", charactersList);
        return "index";
    }

    @RequestMapping("/characters/{id}")
    public String getTemplateWithId(@PathVariable("id") int id, Model model){
        model.addAttribute("character", charactersList.stream().filter(elt -> elt.getId() == id).findFirst().orElse(null));
        return "characterSheet";
    }

    @GetMapping("/characters/add")
    public String showAddCharacter(Model model){
        PlayableCharacter lastId = charactersList.get(charactersList.size()-1);

        PlayableCharacter newCharacter = new PlayableCharacter(lastId.getId() + 1);
        model.addAttribute("newCharacter", newCharacter);
        List<String> typeList = Arrays.asList("Démonsite", "Magicien", "Guerrier", "Voleur");
        model.addAttribute("typeList", typeList);
        return "addCharacter";
    }

    @PostMapping("/characters/add")
    public String submitCharacterForm(@ModelAttribute("newCharacter") Object newCharacter, HttpServletRequest request){
        return "index";
    }

}
