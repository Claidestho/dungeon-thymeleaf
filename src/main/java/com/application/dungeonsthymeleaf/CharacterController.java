package com.application.dungeonsthymeleaf;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


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
        return "charactersList";
    }

    @RequestMapping("/characters/{id}")
    public String getTemplateWithId(@PathVariable("id") int id, Model model){
        model.addAttribute("character", charactersList.stream().filter(elt -> elt.getId() == id).findFirst().orElse(null));
        return "characterSheet";
    }

    @GetMapping("/characters/add")
    public String showAddCharacter(Model model){
        PlayableCharacter newCharacter = new PlayableCharacter(0, "Nom du héros", "Type de héros", 0);
        model.addAttribute("newCharacter", newCharacter);
        List<String> typeList = Arrays.asList("Démonsite", "Magicien", "Guerrier", "Voleur");
        model.addAttribute("typeList", typeList);
        return "addCharacter";
    }

    @PostMapping("/characters/add")
    public String submitCharacterForm(@ModelAttribute("newCharacter") PlayableCharacter newCharacter, HttpServletRequest request){
        String name = newCharacter.getName();
        String type = newCharacter.getType();
        int healthPoints = newCharacter.getHealthPoints();
        PlayableCharacter lastId = charactersList.get(charactersList.size()-1);
        PlayableCharacter newNewCharacter = new PlayableCharacter(lastId.getId() + 1, name, type, healthPoints);
        charactersList.add(newNewCharacter);
        return "redirect:/characters/";
    }

}
