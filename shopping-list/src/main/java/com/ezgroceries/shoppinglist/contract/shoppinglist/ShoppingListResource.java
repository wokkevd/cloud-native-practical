package com.ezgroceries.shoppinglist.contract.shoppinglist;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(of = "shoppingListId")
public class ShoppingListResource {

    private final UUID shoppingListId;
    private String name;
    private List<String> ingredients = new ArrayList<>();

    public ShoppingListResource(UUID shoppingListId, String name) {
        this.shoppingListId = shoppingListId;
        this.name = name;
    }
}
