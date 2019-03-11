package com.ezgroceries.shoppinglist.factory;

import com.ezgroceries.shoppinglist.contract.cocktaildb.CocktailDBDrinkResource;
import com.ezgroceries.shoppinglist.domain.CocktailEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CocktailEntityFactory {

    public CocktailEntity create(CocktailDBDrinkResource drinkResource) {
        CocktailEntity cocktailEntity = CocktailEntity.builder()
                .drinkId(drinkResource.getIdDrink())
                .name(drinkResource.getStrDrink())
                .glass(drinkResource.getStrGlass())
                .instructions(drinkResource.getStrInstructions())
                .url(drinkResource.getStrDrinkThumb())
                .build();
        List<String> ingredients = cocktailEntity.getIngredients();
        addIngredient(ingredients, drinkResource.getStrIngredient1());
        addIngredient(ingredients, drinkResource.getStrIngredient2());
        addIngredient(ingredients, drinkResource.getStrIngredient3());
        addIngredient(ingredients, drinkResource.getStrIngredient4());
        addIngredient(ingredients, drinkResource.getStrIngredient5());
        addIngredient(ingredients, drinkResource.getStrIngredient6());
        addIngredient(ingredients, drinkResource.getStrIngredient7());
        return cocktailEntity;
    }

    private void addIngredient(List<String> ingredients, String ingredient) {
        if (!StringUtils.isEmpty(ingredient)) {
            ingredients.add(ingredient);
        }
    }

    public List<CocktailEntity> create(List<CocktailDBDrinkResource> drinkResources) {
        return drinkResources.stream().map(this::create).collect(Collectors.toList());
    }
}
