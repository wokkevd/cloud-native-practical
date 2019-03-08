package com.ezgroceries.shoppinglist.contract.cocktaildb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CocktailDBResponseResource {

    private List<CocktailDBDrinkResource> drinks;
}
