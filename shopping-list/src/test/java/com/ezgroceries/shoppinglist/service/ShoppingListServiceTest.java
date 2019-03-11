package com.ezgroceries.shoppinglist.service;

import com.ezgroceries.shoppinglist.contract.shoppinglist.ShoppingListResource;
import com.ezgroceries.shoppinglist.domain.CocktailEntity;
import com.ezgroceries.shoppinglist.domain.ShoppingListEntity;
import com.ezgroceries.shoppinglist.factory.ShoppingListResourceFactory;
import com.ezgroceries.shoppinglist.repository.CocktailRepository;
import com.ezgroceries.shoppinglist.repository.ShoppingListRepository;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShoppingListServiceTest {

    private static final UUID SHOPPING_LIST_ID_1 = UUID.randomUUID();
    private static final UUID SHOPPING_LIST_ID_2 = UUID.randomUUID();
    private static final UUID SHOPPING_LIST_ID_X = UUID.randomUUID();
    private static final String SHOPPING_LIST_NAME_1 = "Stephanie's birthday";
    private static final String SHOPPING_LIST_NAME_2 = "Nick's birthday";
    private static final String CREATE_LIST_NAME = "Test List";

    private static final UUID FAKE_COCKTAIL_ID = UUID.randomUUID();
    private static final UUID EXISTING_COCKTAIL_ID_1 = UUID.randomUUID();
    private static final String DRINK_ID_1 = "123";
    private static final String DRINK_NAME_1 = "Margerita";
    private static final String DRINK_INSTRUCTIONS_1 = "Rub the rim of the glass with the lime slice to make the salt stick to it. Take care to moisten..";
    private static final String DRINK_URL_1 = "https://www.thecocktaildb.com/images/media/drink/wpxpvu1439905379.jpg";
    private static final List<String> INGREDIENTS_1 = asList("Tequila", "Triple sec", "Lime juice", "Salt");
    private static final UUID EXISTING_COCKTAIL_ID_2 = UUID.randomUUID();
    private static final String DRINK_ID_2 = "456";
    private static final String DRINK_NAME_2 = "Blue Margerita";
    private static final String DRINK_INSTRUCTIONS_2 = "Rub rim of cocktail glass with lime juice. Dip rim in coarse salt..";
    private static final String DRINK_URL_2 = "https://www.thecocktaildb.com/images/media/drink/qtvvyq1439905913.jpg";
    private static final List<String> INGREDIENTS_2 = asList("Tequila", "Blue Curacao", "Lime juice", "Salt");
    private static final String GLASS = "Cocktail glass";

    @Autowired
    private ShoppingListService shoppingListService;
    @MockBean
    private ShoppingListRepository shoppingListRepository;
    @MockBean
    private CocktailRepository cocktailRepository;
    @SpyBean
    private ShoppingListResourceFactory shoppingListResourceFactory;

    @Before
    public void setUp() {
        CocktailEntity cocktailEntity1 = CocktailEntity.builder()
                .id(EXISTING_COCKTAIL_ID_1)
                .drinkId(DRINK_ID_1)
                .name(DRINK_NAME_1)
                .glass(GLASS)
                .instructions(DRINK_INSTRUCTIONS_1)
                .url(DRINK_URL_1)
                .ingredients(INGREDIENTS_1)
                .build();
        CocktailEntity cocktailEntity2 = CocktailEntity.builder()
                .id(EXISTING_COCKTAIL_ID_2)
                .drinkId(DRINK_ID_2)
                .name(DRINK_NAME_2)
                .glass(GLASS)
                .instructions(DRINK_INSTRUCTIONS_2)
                .url(DRINK_URL_2)
                .ingredients(INGREDIENTS_2)
                .build();
        ShoppingListEntity shoppingListEntity1 = new ShoppingListEntity(SHOPPING_LIST_ID_1, SHOPPING_LIST_NAME_1, Sets.newHashSet(cocktailEntity2));
        ShoppingListEntity shoppingListEntity2 = new ShoppingListEntity(SHOPPING_LIST_ID_2, SHOPPING_LIST_NAME_2, Sets.newHashSet(cocktailEntity1, cocktailEntity2));
        when(cocktailRepository.findById(EXISTING_COCKTAIL_ID_1)).thenReturn(Optional.of(cocktailEntity1));
        when(shoppingListRepository.findById(SHOPPING_LIST_ID_1)).thenReturn(Optional.of(shoppingListEntity1));
        when(shoppingListRepository.findAll()).thenReturn(Arrays.asList(shoppingListEntity1, shoppingListEntity2));
    }

    @Test
    public void createShoppingList() {
        ShoppingListEntity createdShoppingList = new ShoppingListEntity(CREATE_LIST_NAME);
        when(shoppingListRepository.save(any())).thenReturn(createdShoppingList);
        ShoppingListResource shoppingList = shoppingListService.createShoppingList(CREATE_LIST_NAME);
        assertThat(shoppingList).isNotNull();
        assertThat(shoppingList.getShoppingListId()).isEqualTo(createdShoppingList.getId());
        assertThat(shoppingList.getName()).isEqualTo(CREATE_LIST_NAME);
        assertThat(shoppingList.getIngredients()).isEmpty();
    }

    @Test
    public void addCocktails() {
        List<UUID> addedCocktails = shoppingListService.addCocktails(SHOPPING_LIST_ID_1, Arrays.asList(EXISTING_COCKTAIL_ID_1,
                EXISTING_COCKTAIL_ID_2, FAKE_COCKTAIL_ID));
        assertThat(addedCocktails).hasSize(1);
        assertThat(addedCocktails).containsExactly(EXISTING_COCKTAIL_ID_1);
    }

    @Test
    public void getShoppingList() {
        ShoppingListResource shoppingList = shoppingListService.getShoppingList(SHOPPING_LIST_ID_1);
        assertThat(shoppingList).isNotNull();
        assertThat(shoppingList.getShoppingListId()).isEqualTo(SHOPPING_LIST_ID_1);
        assertThat(shoppingList.getName()).isEqualTo(SHOPPING_LIST_NAME_1);
        assertThat(shoppingList.getIngredients()).isEqualTo(INGREDIENTS_2);
    }

    @Test
    public void getNonExistingShoppingList() {
        ShoppingListResource shoppingList = shoppingListService.getShoppingList(SHOPPING_LIST_ID_X);
        assertThat(shoppingList).isNull();
    }

    @Test
    public void getAllShoppingLists() {
        List<ShoppingListResource> allShoppingLists = shoppingListService.getAllShoppingLists();
        assertThat(allShoppingLists).hasSize(2);
        assertThat(allShoppingLists.get(0).getShoppingListId()).isEqualTo(SHOPPING_LIST_ID_1);
        assertThat(allShoppingLists.get(0).getName()).isEqualTo(SHOPPING_LIST_NAME_1);
        assertThat(allShoppingLists.get(0).getIngredients()).containsExactlyInAnyOrder("Tequila", "Blue Curacao", "Lime juice", "Salt");
        assertThat(allShoppingLists.get(1).getShoppingListId()).isEqualTo(SHOPPING_LIST_ID_2);
        assertThat(allShoppingLists.get(1).getName()).isEqualTo(SHOPPING_LIST_NAME_2);
        assertThat(allShoppingLists.get(1).getIngredients()).containsExactlyInAnyOrder("Tequila", "Blue Curacao", "Lime juice", "Salt", "Triple sec");
    }
}