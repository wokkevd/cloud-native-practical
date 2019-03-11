package com.ezgroceries.shoppinglist.factory;

import com.ezgroceries.shoppinglist.contract.cocktaildb.CocktailDBDrinkResource;
import com.ezgroceries.shoppinglist.domain.CocktailEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CocktailEntityFactoryTest {

    private static final String DRINK_ID_1 = "123";
    private static final String DRINK_ID_2 = "456";
    private static final String DRINK_NAME_1 = "Margerita";
    private static final String DRINK_INSTRUCTIONS_1 = "Rub the rim of the glass with the lime slice to make the salt stick to it. Take care to moisten..";
    private static final String DRINK_URL_1 = "https://www.thecocktaildb.com/images/media/drink/wpxpvu1439905379.jpg";
    private static final String DRINK_NAME_2 = "Blue Margerita";
    private static final String DRINK_INSTRUCTIONS_2 = "Rub rim of cocktail glass with lime juice. Dip rim in coarse salt..";
    private static final String DRINK_URL_2 = "https://www.thecocktaildb.com/images/media/drink/qtvvyq1439905913.jpg";
    private static final String GLASS = "Cocktail glass";

    @Autowired
    private CocktailEntityFactory cocktailEntityFactory;

    @Test
    public void create() {
        CocktailDBDrinkResource drinkResource1 = new CocktailDBDrinkResource(
                DRINK_ID_1,
                DRINK_NAME_1,
                GLASS,
                DRINK_INSTRUCTIONS_1,
                DRINK_URL_1,
                "Tequila",
                "Triple sec",
                "Lime juice",
                "Salt",
                null,
                null,
                null);
        CocktailEntity cocktailEntity = cocktailEntityFactory.create(drinkResource1);
        assertThat(cocktailEntity).isNotNull();
        assertThat(cocktailEntity.getId()).isNull();
        assertThat(cocktailEntity.getDrinkId()).isEqualTo(DRINK_ID_1);
        assertThat(cocktailEntity.getName()).isEqualTo(DRINK_NAME_1);
        assertThat(cocktailEntity.getGlass()).isEqualTo(GLASS);
        assertThat(cocktailEntity.getInstructions()).isEqualTo(DRINK_INSTRUCTIONS_1);
        assertThat(cocktailEntity.getUrl()).isEqualTo(DRINK_URL_1);
        assertThat(cocktailEntity.getIngredients()).containsExactlyInAnyOrder("Tequila", "Triple sec", "Lime juice", "Salt");
    }

    @Test
    public void createMultiple() {
        CocktailDBDrinkResource drinkResource1 = new CocktailDBDrinkResource(
                DRINK_ID_1,
                DRINK_NAME_1,
                GLASS,
                DRINK_INSTRUCTIONS_1,
                DRINK_URL_1,
                "Tequila",
                "Triple sec",
                "Lime juice",
                "Salt",
                null,
                null,
                null);
        CocktailDBDrinkResource drinkResource2 = new CocktailDBDrinkResource(
                DRINK_ID_2,
                DRINK_NAME_2,
                GLASS,
                DRINK_INSTRUCTIONS_2,
                DRINK_URL_2,
                "Tequila",
                "Blue Curacao",
                "Lime juice",
                "Salt",
                null,
                null,
                null);
        List<CocktailEntity> cocktailEntities = cocktailEntityFactory.create(Arrays.asList(drinkResource1, drinkResource2));
        assertThat(cocktailEntities).hasSize(2);
        assertThat(cocktailEntities.get(0).getId()).isNull();
        assertThat(cocktailEntities.get(0).getDrinkId()).isEqualTo(DRINK_ID_1);
        assertThat(cocktailEntities.get(0).getName()).isEqualTo(DRINK_NAME_1);
        assertThat(cocktailEntities.get(0).getGlass()).isEqualTo(GLASS);
        assertThat(cocktailEntities.get(0).getInstructions()).isEqualTo(DRINK_INSTRUCTIONS_1);
        assertThat(cocktailEntities.get(0).getUrl()).isEqualTo(DRINK_URL_1);
        assertThat(cocktailEntities.get(0).getIngredients()).containsExactlyInAnyOrder("Tequila", "Triple sec", "Lime juice", "Salt");
        assertThat(cocktailEntities.get(1).getId()).isNull();
        assertThat(cocktailEntities.get(1).getDrinkId()).isEqualTo(DRINK_ID_2);
        assertThat(cocktailEntities.get(1).getName()).isEqualTo(DRINK_NAME_2);
        assertThat(cocktailEntities.get(1).getGlass()).isEqualTo(GLASS);
        assertThat(cocktailEntities.get(1).getInstructions()).isEqualTo(DRINK_INSTRUCTIONS_2);
        assertThat(cocktailEntities.get(1).getUrl()).isEqualTo(DRINK_URL_2);
        assertThat(cocktailEntities.get(1).getIngredients()).containsExactlyInAnyOrder("Tequila", "Blue Curacao", "Lime juice", "Salt");
    }
}