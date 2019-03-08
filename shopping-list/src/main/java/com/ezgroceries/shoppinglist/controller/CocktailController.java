package com.ezgroceries.shoppinglist.controller;

import com.ezgroceries.shoppinglist.client.CocktailDBClient;
import com.ezgroceries.shoppinglist.contract.cocktaildb.CocktailDBResponseResource;
import com.ezgroceries.shoppinglist.contract.shoppinglist.CocktailResource;
import com.ezgroceries.shoppinglist.exceptions.NotFoundException;
import com.ezgroceries.shoppinglist.factory.CocktailResourceFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/cocktails", produces = "application/json")
public class CocktailController {

    private final CocktailDBClient cocktailDBClient;
    private final CocktailResourceFactory cocktailResourceFactory;

    public CocktailController(CocktailDBClient cocktailDBClient, CocktailResourceFactory cocktailResourceFactory) {
        this.cocktailDBClient = cocktailDBClient;
        this.cocktailResourceFactory = cocktailResourceFactory;
    }

    @GetMapping
    public List<CocktailResource> getCocktails(@RequestParam String search) {
        ResponseEntity<CocktailDBResponseResource> cocktailResponse = cocktailDBClient.getCocktailBySearchTerm(search);
        List<CocktailResource> cocktailResources = new ArrayList<>();
        if (cocktailResponse != null && cocktailResponse.getBody() != null && cocktailResponse.getBody().getDrinks() != null) {
            cocktailResources = cocktailResourceFactory.create(cocktailResponse.getBody().getDrinks());
        }
        return cocktailResources;
    }

    @GetMapping("/{id}")
    public CocktailResource getCocktail(@PathVariable String id) {
        Optional<CocktailResource> foundCocktail = getDummyCocktails().stream().filter(cr -> cr.getCocktailId().equals(id)).findFirst();
        if (foundCocktail.isPresent()) {
            return foundCocktail.get();
        } else {
            throw new NotFoundException("Cocktail with id %s not found", id);
        }
    }

    //TODO remove dummy implementation
    private List<CocktailResource> getDummyCocktails() {
        return Arrays.asList(
                CocktailResource.builder()
                        .cocktailId("23b3d85a-3928-41c0-a533-6538a71e17c4")
                        .name("Margerita")
                        .glass("Cocktail glass")
                        .instructions("Rub the rim of the glass with the lime slice to make the salt stick to it. Take care to moisten..")
                        .image("https://www.thecocktaildb.com/images/media/drink/wpxpvu1439905379.jpg")
                        .ingredients(Arrays.asList("Tequila", "Triple sec", "Lime juice", "Salt"))
                        .build(),
                CocktailResource.builder()
                        .cocktailId("d615ec78-fe93-467b-8d26-5d26d8eab073")
                        .name("Blue Margerita")
                        .glass("Cocktail glass")
                        .instructions("Rub rim of cocktail glass with lime juice. Dip rim in coarse salt..")
                        .image("https://www.thecocktaildb.com/images/media/drink/qtvvyq1439905913.jpg")
                        .ingredients(Arrays.asList("Tequila", "Blue Curacao", "Lime juice", "Salt"))
                        .build()
        );
    }
}
