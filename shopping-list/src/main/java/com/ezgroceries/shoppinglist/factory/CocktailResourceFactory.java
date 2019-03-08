package com.ezgroceries.shoppinglist.factory;

import com.ezgroceries.shoppinglist.contract.cocktaildb.CocktailDBDrinkResource;
import com.ezgroceries.shoppinglist.contract.shoppinglist.CocktailResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component
public class CocktailResourceFactory {

    public CocktailResource create(CocktailDBDrinkResource drinkResource) {
        CocktailResource cocktailResource = CocktailResource.builder()
                .cocktailId(drinkResource.getIdDrink())
                .name(drinkResource.getStrDrink())
                .instructions(drinkResource.getStrInstructions())
                .glass(drinkResource.getStrGlass())
                .image(drinkResource.getStrDrinkThumb())
                .build();
        addIngredients(drinkResource, cocktailResource);
        return cocktailResource;
    }

    public List<CocktailResource> create(List<CocktailDBDrinkResource> drinkResources) {
        return drinkResources.stream().map(this::create).collect(Collectors.toList());
    }

    private void addIngredients(CocktailDBDrinkResource drinkResource, CocktailResource cocktailResource) {
        addIngredient(drinkResource::getStrIngredient1, cocktailResource);
        addIngredient(drinkResource::getStrIngredient2, cocktailResource);
        addIngredient(drinkResource::getStrIngredient3, cocktailResource);
        addIngredient(drinkResource::getStrIngredient4, cocktailResource);
        addIngredient(drinkResource::getStrIngredient5, cocktailResource);
        addIngredient(drinkResource::getStrIngredient6, cocktailResource);
        addIngredient(drinkResource::getStrIngredient7, cocktailResource);
    }

    private void addIngredient(Supplier<String> ingredientSupplier, CocktailResource cocktailResource) {
        String ingredient = ingredientSupplier.get();
        if (!StringUtils.isEmpty(ingredient)) {
            cocktailResource.getIngredients().add(ingredient);
        }
    }

}
