package com.ezgroceries.shoppinglist.factory;

import com.ezgroceries.shoppinglist.contract.shoppinglist.CocktailResource;
import com.ezgroceries.shoppinglist.domain.CocktailEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CocktailResourceFactory {

    public CocktailResource create(CocktailEntity cocktailEntity) {
        return CocktailResource.builder()
                .id(cocktailEntity.getId())
                .cocktailId(cocktailEntity.getDrinkId())
                .name(cocktailEntity.getName())
                .instructions(cocktailEntity.getInstructions())
                .glass(cocktailEntity.getGlass())
                .image(cocktailEntity.getUrl())
                .ingredients(cocktailEntity.getIngredients())
                .build();
    }

    public List<CocktailResource> create(List<CocktailEntity> cocktailEntities) {
        return cocktailEntities.stream().map(this::create).collect(Collectors.toList());
    }
}
