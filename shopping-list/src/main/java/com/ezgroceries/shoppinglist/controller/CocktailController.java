package com.ezgroceries.shoppinglist.controller;

import com.ezgroceries.shoppinglist.client.CocktailDBClient;
import com.ezgroceries.shoppinglist.contract.cocktaildb.CocktailDBResponseResource;
import com.ezgroceries.shoppinglist.contract.shoppinglist.CocktailResource;
import com.ezgroceries.shoppinglist.exceptions.NotFoundException;
import com.ezgroceries.shoppinglist.service.CocktailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(value = "/cocktails", produces = "application/json")
public class CocktailController {

    private final CocktailDBClient cocktailDBClient;
    private final CocktailService cocktailService;

    public CocktailController(CocktailDBClient cocktailDBClient, CocktailService cocktailService) {
        this.cocktailDBClient = cocktailDBClient;
        this.cocktailService = cocktailService;
    }

    @GetMapping
    public List<CocktailResource> getCocktails(@RequestParam String search) {
        ResponseEntity<CocktailDBResponseResource> cocktailResponse = cocktailDBClient.getCocktailBySearchTerm(search);
        List<CocktailResource> cocktailResources = new ArrayList<>();
        if (cocktailResponse != null && cocktailResponse.getBody() != null && cocktailResponse.getBody().getDrinks() != null) {
            cocktailResources = cocktailService.persistCocktails(cocktailResponse.getBody().getDrinks());
        }
        return cocktailResources;
    }

    @GetMapping("/{id}")
    public CocktailResource getCocktail(@PathVariable UUID id) {
        Optional<CocktailResource> foundCocktail = cocktailService.findById(id);
        if (foundCocktail.isPresent()) {
            return foundCocktail.get();
        } else {
            throw new NotFoundException("Cocktail with id %s not found", id.toString());
        }
    }
}
