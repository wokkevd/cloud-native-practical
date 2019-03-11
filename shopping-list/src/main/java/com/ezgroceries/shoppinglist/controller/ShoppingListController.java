package com.ezgroceries.shoppinglist.controller;

import com.ezgroceries.shoppinglist.contract.shoppinglist.ShoppingListResource;
import com.ezgroceries.shoppinglist.exceptions.NotFoundException;
import com.ezgroceries.shoppinglist.service.ShoppingListService;
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
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/shopping-lists", produces = "application/json")
public class ShoppingListController {

    private final ShoppingListService shoppingListService;

    public ShoppingListController(ShoppingListService shoppingListService) {
        this.shoppingListService = shoppingListService;
    }

    @GetMapping
    public List<ShoppingListResource> getShoppingLists() {
        return shoppingListService.getAllShoppingLists();
    }

    @GetMapping("/{shoppingListId}")
    public ShoppingListResource getShoppingList(@PathVariable UUID shoppingListId) {
        ShoppingListResource shoppingList = shoppingListService.getShoppingList(shoppingListId);
        if (shoppingList != null) {
            return shoppingList;
        } else {
            throw new NotFoundException("Shopping list with id %s not found", shoppingListId.toString());
        }
    }

    @PostMapping
    public ResponseEntity<ShoppingListResource> createShoppingList(@RequestBody ShoppingListCreationResource requestedResource) {
        ShoppingListResource shoppingList = shoppingListService.createShoppingList(requestedResource.getName());
        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{shoppingListId}")
                .buildAndExpand(shoppingList.getShoppingListId())
                .toUri();
        return ResponseEntity.created(location).body(shoppingList);
    }

    @PostMapping("/{shoppingListId}/cocktails")
    public Resources<CocktailIdResource> addCocktailsToList(@PathVariable UUID shoppingListId,
                                                            @RequestBody List<CocktailIdResource> cocktails) {
        List<UUID> cocktailsIds = cocktails.stream().map(CocktailIdResource::getCocktailId).collect(Collectors.toList());
        List<UUID> addedCocktails = shoppingListService.addCocktails(shoppingListId, cocktailsIds);
        if (addedCocktails != null) {
            return new Resources<>(addedCocktails.stream().map(CocktailIdResource::new).collect(Collectors.toList()));
        } else {
            throw new NotFoundException("Shopping list with id %s not found", shoppingListId.toString());
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class ShoppingListCreationResource {

        private String name;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class CocktailIdResource {

        private UUID cocktailId;
    }
}
