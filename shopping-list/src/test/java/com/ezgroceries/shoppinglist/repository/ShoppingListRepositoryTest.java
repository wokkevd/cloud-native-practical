package com.ezgroceries.shoppinglist.repository;

import com.ezgroceries.shoppinglist.annotation.DBUnitTest;
import com.ezgroceries.shoppinglist.domain.BaseEntity;
import com.ezgroceries.shoppinglist.domain.ShoppingListEntity;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DBUnitTest
@DatabaseSetup({ShoppingListRepositoryTest.DATASET})
@DatabaseTearDown(type = DatabaseOperation.DELETE, value = {ShoppingListRepositoryTest.DATASET})
public class ShoppingListRepositoryTest {

    static final String DATASET = "classpath:com/ezgroceries/shoppinglist/shopping_lists_dataset.xml";

    private static final String LIST_NAME = "Nick's birthday";
    private static final UUID USER_ID = UUID.fromString("d7e711df-c0fb-49d0-86bd-b2472cdad4ca");
    private static final UUID USER_ID_2 = UUID.fromString("e37f50c2-84ce-4fff-9621-be0a3e552ad8");
    private static final UUID COCKTAIL_ID_1 = UUID.fromString("a1fd2e4e-4f49-5d7c-adce-37dd0d8395e7");
    private static final UUID COCKTAIL_ID_2 = UUID.fromString("0eb9f706-964f-5089-a4a4-38d0cbba61f9");
    private static final UUID SHOPPING_LIST_ID = UUID.fromString("1c6c2cb1-8f5e-442d-b79c-7205b689e2f8");

    @Autowired
    private ShoppingListRepository shoppingListRepository;

    @Test
    public void findByUserId() {
        List<ShoppingListEntity> shoppingListEntities = shoppingListRepository.findByUserId(USER_ID);
        assertThat(shoppingListEntities).hasSize(1);
        assertThat(shoppingListEntities.get(0).getUser().getId()).isEqualTo(USER_ID);
        assertThat(shoppingListEntities.get(0).getName()).isEqualTo(LIST_NAME);
        assertThat(shoppingListEntities.get(0).getCocktails()).hasSize(2);
        assertThat(shoppingListEntities.get(0).getCocktails()).extracting(BaseEntity::getId).containsExactlyInAnyOrder(COCKTAIL_ID_1, COCKTAIL_ID_2);
    }

    @Test
    public void findByUnknownUserId() {
        List<ShoppingListEntity> shoppingListEntities = shoppingListRepository.findByUserId(COCKTAIL_ID_1);
        assertThat(shoppingListEntities).isEmpty();
    }

    @Test
    public void findByIdAndUserId() {
        Optional<ShoppingListEntity> optionalShoppingList = shoppingListRepository.findByIdAndUserId(SHOPPING_LIST_ID, USER_ID);
        assertThat(optionalShoppingList).isPresent();
        assertThat(optionalShoppingList.get().getUser().getId()).isEqualTo(USER_ID);
        assertThat(optionalShoppingList.get().getName()).isEqualTo(LIST_NAME);
        assertThat(optionalShoppingList.get().getCocktails()).hasSize(2);
        assertThat(optionalShoppingList.get().getCocktails()).extracting(BaseEntity::getId).containsExactlyInAnyOrder(COCKTAIL_ID_1, COCKTAIL_ID_2);
    }

    @Test
    public void findByUnknownIdAndUserId() {
        Optional<ShoppingListEntity> optionalShoppingList = shoppingListRepository.findByIdAndUserId(USER_ID, USER_ID);
        assertThat(optionalShoppingList).isNotPresent();
    }

    @Test
    public void findByIdAndNonOwnerUserId() {
        Optional<ShoppingListEntity> optionalShoppingList = shoppingListRepository.findByIdAndUserId(SHOPPING_LIST_ID, USER_ID_2);
        assertThat(optionalShoppingList).isNotPresent();
    }
}