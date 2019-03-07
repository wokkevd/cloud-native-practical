package com.ezgroceries.shoppinglist.controller;

import com.ezgroceries.shoppinglist.contract.CocktailResource;
import com.ezgroceries.shoppinglist.exceptions.NotFoundException;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/cocktails", produces = "application/json")
public class CocktailController {

    public Resources<CocktailResource> getCocktails() {
        return getCocktails(null);
    }

    @GetMapping
    public Resources<CocktailResource> getCocktails(@RequestParam(required = false) String search) {
        if (StringUtils.isBlank(search)) {
            return new Resources<>(getDummyCocktails());
        } else {
            return new Resources<>(getDummyCocktails().stream().filter(cr -> cr.getName().contains(search)).collect(Collectors.toList()));
        }
    }

    @GetMapping("/{name}")
    public CocktailResource getCocktail(@PathVariable String name) {
        Optional<CocktailResource> foundCocktail = getDummyCocktails().stream().filter(cr -> cr.getName().equals(name)).findFirst();
        if (foundCocktail.isPresent()) {
            return foundCocktail.get();
        } else {
            throw new NotFoundException("Cocktail with name %s not found", name);
        }
    }

    //TODO remove dummy implementation
    private List<CocktailResource> getDummyCocktails() {
        return Arrays.asList(
                CocktailResource.builder()
                        .cocktailId(UUID.fromString("23b3d85a-3928-41c0-a533-6538a71e17c4"))
                        .name("Margerita")
                        .glass("Cocktail glass")
                        .instructions("Rub the rim of the glass with the lime slice to make the salt stick to it. Take care to moisten..")
                        .image("https://www.thecocktaildb.com/images/media/drink/wpxpvu1439905379.jpg")
                        .ingredients(Arrays.asList("Tequila", "Triple sec", "Lime juice", "Salt"))
                        .build(),
                CocktailResource.builder()
                        .cocktailId(UUID.fromString("d615ec78-fe93-467b-8d26-5d26d8eab073"))
                        .name("Blue Margerita")
                        .glass("Cocktail glass")
                        .instructions("Rub rim of cocktail glass with lime juice. Dip rim in coarse salt..")
                        .image("https://www.thecocktaildb.com/images/media/drink/qtvvyq1439905913.jpg")
                        .ingredients(Arrays.asList("Tequila", "Blue Curacao", "Lime juice", "Salt"))
                        .build()
        );
    }

}
