package com.ezgroceries.shoppinglist.factory;

import com.ezgroceries.shoppinglist.contract.shoppinglist.ShoppingListResource;
import com.ezgroceries.shoppinglist.domain.CocktailEntity;
import com.ezgroceries.shoppinglist.domain.ShoppingListEntity;
import com.google.common.collect.Sets;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShoppingListResourceFactoryTest {

    private static final UUID SHOPPING_LIST_ID_1 = UUID.randomUUID();
    private static final UUID SHOPPING_LIST_ID_2 = UUID.randomUUID();
    private static final UUID COCKTAIL_ID_1 = UUID.randomUUID();
    private static final UUID COCKTAIL_ID_2 = UUID.randomUUID();
    private static final String SHOPPING_LIST_NAME_1 = "Stephanie's birthday";
    private static final String SHOPPING_LIST_NAME_2 = "Nick's birthday";
    private static final List<String> INGREDIENTS_1 = Arrays.asList("Tequila", "Triple sec", "Lime juice", "Salt", "Blue Curacao");
    private static final List<String> INGREDIENTS_2 = Arrays.asList("Vodka", "Coffee liqueur", "Light cream");

    @Autowired
    private ShoppingListResourceFactory shoppingListResourceFactory;

    @Test
    public void create() {
        CocktailEntity cocktailEntity1 = new CocktailEntity();
        cocktailEntity1.setIngredients(INGREDIENTS_1);
        ShoppingListEntity shoppingListEntity1 = new ShoppingListEntity(SHOPPING_LIST_NAME_1);
        shoppingListEntity1.setId(SHOPPING_LIST_ID_1);
        shoppingListEntity1.setCocktails(Sets.newHashSet(cocktailEntity1));
        ShoppingListResource shoppingListResource = shoppingListResourceFactory.create(shoppingListEntity1);
        assertThat(shoppingListResource).isNotNull();
        assertThat(shoppingListResource.getShoppingListId()).isEqualTo(SHOPPING_LIST_ID_1);
        assertThat(shoppingListResource.getName()).isEqualTo(SHOPPING_LIST_NAME_1);
        assertThat(shoppingListResource.getIngredients()).containsExactlyInAnyOrder("Tequila", "Triple sec", "Lime juice", "Salt", "Blue Curacao");
    }

    @Test
    public void createMultiple() {
        CocktailEntity cocktailEntity1 = new CocktailEntity();
        cocktailEntity1.setId(COCKTAIL_ID_1);
        cocktailEntity1.setIngredients(INGREDIENTS_1);
        ShoppingListEntity shoppingListEntity1 = new ShoppingListEntity(SHOPPING_LIST_NAME_1);
        shoppingListEntity1.setId(SHOPPING_LIST_ID_1);
        shoppingListEntity1.setCocktails(Sets.newHashSet(cocktailEntity1));
        CocktailEntity cocktailEntity2 = new CocktailEntity();
        cocktailEntity2.setId(COCKTAIL_ID_2);
        cocktailEntity2.setIngredients(INGREDIENTS_2);
        ShoppingListEntity shoppingListEntity2 = new ShoppingListEntity(SHOPPING_LIST_NAME_2);
        shoppingListEntity2.setId(SHOPPING_LIST_ID_2);
        shoppingListEntity2.setCocktails(Sets.newHashSet(cocktailEntity1, cocktailEntity2));

        List<ShoppingListResource> shoppingListResources = shoppingListResourceFactory.create(Arrays.asList(shoppingListEntity1, shoppingListEntity2));
        assertThat(shoppingListResources).hasSize(2);
        assertThat(shoppingListResources.get(0).getShoppingListId()).isEqualTo(SHOPPING_LIST_ID_1);
        assertThat(shoppingListResources.get(0).getName()).isEqualTo(SHOPPING_LIST_NAME_1);
        assertThat(shoppingListResources.get(0).getIngredients()).containsExactlyInAnyOrder("Tequila", "Triple sec", "Lime juice", "Salt", "Blue Curacao");
        assertThat(shoppingListResources.get(1).getShoppingListId()).isEqualTo(SHOPPING_LIST_ID_2);
        assertThat(shoppingListResources.get(1).getName()).isEqualTo(SHOPPING_LIST_NAME_2);
        assertThat(shoppingListResources.get(1).getIngredients()).containsExactlyInAnyOrder("Tequila", "Triple sec", "Lime juice", "Salt",
                "Blue Curacao", "Vodka", "Coffee liqueur", "Light cream");
    }
}