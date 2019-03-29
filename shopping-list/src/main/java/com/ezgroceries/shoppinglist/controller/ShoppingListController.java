package com.ezgroceries.shoppinglist.controller;

import com.ezgroceries.shoppinglist.contract.shoppinglist.ShoppingListResource;
import com.ezgroceries.shoppinglist.service.ShoppingListService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/users/{userId}/shopping-lists", produces = "application/json")
public class ShoppingListController {

    private final ShoppingListService shoppingListService;

    public ShoppingListController(ShoppingListService shoppingListService) {
        this.shoppingListService = shoppingListService;
    }

    @GetMapping
    public List<ShoppingListResource> getShoppingLists(@PathVariable UUID userId) {
        return shoppingListService.getAllShoppingLists(userId);
    }

    @GetMapping("/{shoppingListId}")
    public ShoppingListResource getShoppingList(@PathVariable UUID userId, @PathVariable UUID shoppingListId) {
        return shoppingListService.getShoppingList(shoppingListId, userId);
    }

    @PostMapping
    public ResponseEntity<ShoppingListResource> createShoppingList(@PathVariable UUID userId,
                                                                   @Valid @RequestBody ShoppingListCreationResource requestedResource) {
        ShoppingListResource shoppingList = shoppingListService.createShoppingList(requestedResource.getName(), userId);
        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{shoppingListId}")
                .buildAndExpand(shoppingList.getShoppingListId())
                .toUri();
        return ResponseEntity.created(location).body(shoppingList);
    }

    @PostMapping("/{shoppingListId}/cocktails")
    public List<CocktailIdResource> addCocktailsToList(@PathVariable UUID userId,
                                                       @PathVariable UUID shoppingListId,
                                                       @Valid @RequestBody List<CocktailIdResource> cocktails) {
        List<UUID> cocktailsIds = cocktails.stream().map(CocktailIdResource::getCocktailId).collect(Collectors.toList());
        return shoppingListService.addCocktails(shoppingListId, userId, cocktailsIds).stream()
                .map(CocktailIdResource::new)
                .collect(Collectors.toList());
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class ShoppingListCreationResource {

        @NotNull
        private String name;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class CocktailIdResource {

        @NotNull
        private UUID cocktailId;
    }
}
