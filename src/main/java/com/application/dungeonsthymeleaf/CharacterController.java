package com.application.dungeonsthymeleaf;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Controller
public class CharacterController {
    public List<Character> charactersList;
    public RestTemplate restTemplate = new RestTemplate();

    public CharacterController() {
        this.charactersList = new ArrayList<>();
        charactersList.add(0, new Character(1, "Loris", "Magicien", 12));
        charactersList.add(1, new Character(2, "Allan", "Prêtre", 8));
        charactersList.add(2, new Character(3, "Jipékof", "Démoniste", 20));
    }

    // Affichage de la liste des personnages
    @RequestMapping("/characters")
    public String getCharactersList(Model model) {
        Character[] allCharacters = restTemplate.getForObject("http://localhost:8081/characters", Character[].class);
        model.addAttribute("characters", allCharacters);
        return "charactersList";
    }

    // Affichage de la feuille de personnage
    @RequestMapping("/characters/{id}")
    public String getCharacterById(@PathVariable("id") int id, Model model) {
        Character character = restTemplate.getForObject("http://localhost:8081/characters/" + id, Character.class);
        model.addAttribute("character", character);
        return "characterSheet";
    }

    // Affichage du formulaire d'ajout
    @GetMapping("/characters/add")
    public String showAddCharacter(Model model) {
        Character newCharacter = new Character(0, "Nom du héros", "Type de héros", 0);
        model.addAttribute("newCharacter", newCharacter);
        List<String> typeList = Arrays.asList("Démonsite", "Magicien", "Guerrier", "Voleur");
        model.addAttribute("typeList", typeList);
        return "addCharacter";
    }

    // Récupération de la requête POST du formulaire
    @PostMapping("/characters/add")
    public String submitCharacterForm(@ModelAttribute("newCharacter") Character newCharacter, HttpServletRequest request) throws URISyntaxException {
        Character newNewCharacter = new Character();
        newNewCharacter.setName(newCharacter.getName());
        newNewCharacter.setType(newCharacter.getType());
        newNewCharacter.setHealthPoints(newCharacter.getHealthPoints());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        URI uri = new URI("http://localhost:8081/characters");
        HttpEntity<Character> httpEntity = new HttpEntity<>(newNewCharacter, headers);

        newNewCharacter = restTemplate.postForObject(uri, httpEntity, Character.class);
        return "redirect:/characters/";
    }

    @GetMapping("/characters/edit/{id}")
    public String showEditCharacter(Model model, @PathVariable("id") int id) {
        model.addAttribute("character", charactersList.stream().filter(elt -> elt.getId() == id).findFirst().orElse(null));
        return "editCharacter";
    }

    @PutMapping("/characters/edit/{id}")
    public String submitEditForm(@ModelAttribute("character") Character editedCharacter, @PathVariable("id") int id) throws URISyntaxException {
        editedCharacter.setId(id);
       restTemplate.put("http://localhost:8081/characters/" + id, editedCharacter, Character.class);
        return "redirect:/characters";
    }

    @DeleteMapping("/characters/delete/{id}")
    public String deleteCharacter(@PathVariable("id") int id) {
        restTemplate.delete("http://localhost:8081/characters/" + id);
        return "redirect:/characters";
    }

}
