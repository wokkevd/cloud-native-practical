package com.ezgroceries.shoppinglist.controller;

import com.ezgroceries.shoppinglist.client.CocktailDBClient;
import com.ezgroceries.shoppinglist.contract.cocktaildb.CocktailDBResponseResource;
import com.ezgroceries.shoppinglist.contract.shoppinglist.CocktailResource;
import com.ezgroceries.shoppinglist.service.CocktailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
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
        if (cocktailResponse != null && cocktailResponse.getBody() != null && cocktailResponse.getBody().getDrinks() != null) {
            return cocktailService.persistCocktails(cocktailResponse.getBody().getDrinks());
        }
        return new ArrayList<>();
    }

    @GetMapping("/{id}")
    public CocktailResource getCocktail(@PathVariable UUID id) {
        return cocktailService.findById(id);
    }
}
