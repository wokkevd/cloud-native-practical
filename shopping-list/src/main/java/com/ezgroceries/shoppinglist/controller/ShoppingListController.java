package com.ezgroceries.shoppinglist.controller;

import com.ezgroceries.shoppinglist.contract.shoppinglist.CocktailResource;
import com.ezgroceries.shoppinglist.contract.shoppinglist.ShoppingListResource;
import com.ezgroceries.shoppinglist.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/shopping-lists", produces = "application/json")
public class ShoppingListController {

    private final CocktailController cocktailController;

    //TODO remove dummy mechanism
    private Map<UUID, ShoppingListResource> createdShoppingLists;

    public ShoppingListController(CocktailController cocktailController) {
        this.cocktailController = cocktailController;
        //TODO remove dummy data
        initDummyData();
    }

    void initDummyData() {
        createdShoppingLists = new HashMap<>();
        ShoppingListResource dummyShoppingList = new ShoppingListResource(UUID.fromString("eb18bb7c-61f3-4c9f-981c-55b1b8ee8915"), "Stephanie's birthday");
        dummyShoppingList.getIngredients().addAll(Arrays.asList("Tequila", "Triple sec", "Lime juice", "Salt", "Blue Curacao"));
        createdShoppingLists.put(dummyShoppingList.getShoppingListId(), dummyShoppingList);
    }

    @GetMapping
    public Resources<ShoppingListResource> getShoppingLists() {
        return new Resources<>(createdShoppingLists.values());
    }

    @GetMapping("/{shoppingListId}")
    public ShoppingListResource getShoppingList(@PathVariable UUID shoppingListId) {
        if (createdShoppingLists.containsKey(shoppingListId)) {
            return createdShoppingLists.get(shoppingListId);
        } else {
            throw new NotFoundException("Shopping list with id %s not found", shoppingListId.toString());
        }
    }

    @PostMapping
    public ResponseEntity<ShoppingListCreationResource> createShoppingList(@RequestBody ShoppingListResource requestedResource) {
        UUID shoppingListId = UUID.randomUUID();
        createdShoppingLists.put(shoppingListId, new ShoppingListResource(shoppingListId, requestedResource.getName()));
        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{shoppingListId}")
                .buildAndExpand(shoppingListId)
                .toUri();
        return ResponseEntity.created(location).body(new ShoppingListCreationResource(shoppingListId, requestedResource.getName()));
    }

    @PostMapping("/{shoppingListId}/cocktails")
    public Resources<CocktailIdResource> addCocktailsToList(@PathVariable UUID shoppingListId,
                                                            @RequestBody List<CocktailIdResource> cocktails) {
        ShoppingListResource shoppingList = getShoppingList(shoppingListId);
        List<CocktailResource> availableCocktails = cocktails.stream()
                .map(cir -> cocktailController.getCocktail(cir.getCocktailId()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        shoppingList.getIngredients().addAll(availableCocktails.stream()
                .map(CocktailResource::getIngredients)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList())
        );
        return new Resources<>(availableCocktails.stream()
                .map(CocktailResource::getCocktailId)
                .map(CocktailIdResource::new)
                .collect(Collectors.toList()));
    }

    @Data
    @AllArgsConstructor
    static class ShoppingListCreationResource {

        private UUID shoppingListId;
        private String name;

        ShoppingListCreationResource(String name) {
            this.name = name;
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class CocktailIdResource {

        private String cocktailId;
    }
}
