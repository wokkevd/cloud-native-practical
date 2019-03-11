package com.ezgroceries.shoppinglist.factory;

import com.ezgroceries.shoppinglist.contract.shoppinglist.ShoppingListResource;
import com.ezgroceries.shoppinglist.domain.CocktailEntity;
import com.ezgroceries.shoppinglist.domain.ShoppingListEntity;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ShoppingListResourceFactory {

    public ShoppingListResource create(ShoppingListEntity shoppingListEntity) {
        ShoppingListResource shoppingListResource = new ShoppingListResource(shoppingListEntity.getId(), shoppingListEntity.getName());
        shoppingListResource.getIngredients().addAll(
                shoppingListEntity.getCocktails().stream()
                        .map(CocktailEntity::getIngredients)
                        .flatMap(Collection::stream)
                        .distinct()
                        .collect(Collectors.toList()));
        return shoppingListResource;
    }

    public List<ShoppingListResource> create(List<ShoppingListEntity> shoppingListEntities) {
        return shoppingListEntities.stream().map(this::create).collect(Collectors.toList());
    }
}
