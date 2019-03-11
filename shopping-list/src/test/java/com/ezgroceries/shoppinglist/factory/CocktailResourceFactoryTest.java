package com.ezgroceries.shoppinglist.factory;

import com.ezgroceries.shoppinglist.contract.shoppinglist.CocktailResource;
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
public class CocktailResourceFactoryTest {

    private static final String DRINK_1_ID = "23b3d85a-3928-41c0-a533-6538a71e17c4";
    private static final String DRINK_1_NAME = "Margerita";
    private static final String DRINK_1_GLASS = "Cocktail glass";
    private static final String DRINK_1_INSTRUCTIONS = "Rub the rim of the glass with the lime slice to make the salt stick to it. Take care to moisten..";
    private static final String DRINK_1_DRINK_THUMB = "https://www.thecocktaildb.com/images/media/drink/wpxpvu1439905379.jpg";
    private static final String DRINK_1_INGREDIENT_1 = "Tequila";
    private static final String DRINK_1_INGREDIENT_2 = "Triple sec";
    private static final String DRINK_1_INGREDIENT_3 = "Lime juice";
    private static final String DRINK_1_INGREDIENT_4 = "Salt";
    private static final String DRINK_2_ID = "d615ec78-fe93-467b-8d26-5d26d8eab073";
    private static final String DRINK_2_NAME = "Blue Margerita";
    private static final String DRINK_2_GLASS = "Cocktail glass";
    private static final String DRINK_2_INSTRUCTIONS = "Rub rim of cocktail glass with lime juice. Dip rim in coarse salt..";
    private static final String DRINK_2_DRINK_THUMB = "https://www.thecocktaildb.com/images/media/drink/qtvvyq1439905913.jpg";
    private static final String DRINK_2_INGREDIENT_1 = "Tequila";
    private static final String DRINK_2_INGREDIENT_2 = "Blue Curacao";
    private static final String DRINK_2_INGREDIENT_3 = "Lime juice";
    private static final String DRINK_2_INGREDIENT_4 = "Salt";
    private static final String DRINK_2_INGREDIENT_5 = "Air";
    private static final String DRINK_2_INGREDIENT_6 = "Ice";
    private static final String DRINK_2_INGREDIENT_7 = "Happiness";

    @Autowired
    private CocktailResourceFactory cocktailResourceFactory;

    @Test
    public void create() {
        CocktailEntity drinkResource1 = CocktailEntity.builder()
                .drinkId(DRINK_1_ID)
                .name(DRINK_1_NAME)
                .glass(DRINK_1_GLASS)
                .instructions(DRINK_1_INSTRUCTIONS)
                .url(DRINK_1_DRINK_THUMB)
                .ingredients(Arrays.asList(DRINK_1_INGREDIENT_1, DRINK_1_INGREDIENT_2, DRINK_1_INGREDIENT_3, DRINK_1_INGREDIENT_4))
                .build();

        CocktailResource cocktailResource = cocktailResourceFactory.create(drinkResource1);
        assertThat(cocktailResource).isNotNull();
        assertThat(cocktailResource.getCocktailId()).isEqualTo(DRINK_1_ID);
        assertThat(cocktailResource.getName()).isEqualTo(DRINK_1_NAME);
        assertThat(cocktailResource.getGlass()).isEqualTo(DRINK_1_GLASS);
        assertThat(cocktailResource.getInstructions()).isEqualTo(DRINK_1_INSTRUCTIONS);
        assertThat(cocktailResource.getImage()).isEqualTo(DRINK_1_DRINK_THUMB);
        assertThat(cocktailResource.getIngredients()).containsExactly(DRINK_1_INGREDIENT_1, DRINK_1_INGREDIENT_2, DRINK_1_INGREDIENT_3, DRINK_1_INGREDIENT_4);
    }

    @Test
    public void createMultiple() {
        CocktailEntity drinkResource1 = CocktailEntity.builder()
                .drinkId(DRINK_1_ID)
                .name(DRINK_1_NAME)
                .glass(DRINK_1_GLASS)
                .instructions(DRINK_1_INSTRUCTIONS)
                .url(DRINK_1_DRINK_THUMB)
                .ingredients(Arrays.asList(DRINK_1_INGREDIENT_1, DRINK_1_INGREDIENT_2, DRINK_1_INGREDIENT_3, DRINK_1_INGREDIENT_4))
                .build();
        CocktailEntity drinkResource2 = CocktailEntity.builder()
                .drinkId(DRINK_2_ID)
                .name(DRINK_2_NAME)
                .glass(DRINK_2_GLASS)
                .instructions(DRINK_2_INSTRUCTIONS)
                .url(DRINK_2_DRINK_THUMB)
                .ingredients(Arrays.asList(DRINK_2_INGREDIENT_1, DRINK_2_INGREDIENT_2, DRINK_2_INGREDIENT_3, DRINK_2_INGREDIENT_4,
                        DRINK_2_INGREDIENT_5, DRINK_2_INGREDIENT_6, DRINK_2_INGREDIENT_7))
                .build();

        List<CocktailResource> cocktailResource = cocktailResourceFactory.create(Arrays.asList(drinkResource1, drinkResource2));
        assertThat(cocktailResource).hasSize(2);
        assertThat(cocktailResource.get(0).getCocktailId()).isEqualTo(DRINK_1_ID);
        assertThat(cocktailResource.get(0).getName()).isEqualTo(DRINK_1_NAME);
        assertThat(cocktailResource.get(0).getGlass()).isEqualTo(DRINK_1_GLASS);
        assertThat(cocktailResource.get(0).getInstructions()).isEqualTo(DRINK_1_INSTRUCTIONS);
        assertThat(cocktailResource.get(0).getImage()).isEqualTo(DRINK_1_DRINK_THUMB);
        assertThat(cocktailResource.get(0).getIngredients()).containsExactly(DRINK_1_INGREDIENT_1, DRINK_1_INGREDIENT_2, DRINK_1_INGREDIENT_3, DRINK_1_INGREDIENT_4);
        assertThat(cocktailResource.get(1).getCocktailId()).isEqualTo(DRINK_2_ID);
        assertThat(cocktailResource.get(1).getName()).isEqualTo(DRINK_2_NAME);
        assertThat(cocktailResource.get(1).getGlass()).isEqualTo(DRINK_2_GLASS);
        assertThat(cocktailResource.get(1).getInstructions()).isEqualTo(DRINK_2_INSTRUCTIONS);
        assertThat(cocktailResource.get(1).getImage()).isEqualTo(DRINK_2_DRINK_THUMB);
        assertThat(cocktailResource.get(1).getIngredients()).containsExactly(DRINK_2_INGREDIENT_1, DRINK_2_INGREDIENT_2, DRINK_2_INGREDIENT_3,
                DRINK_2_INGREDIENT_4, DRINK_2_INGREDIENT_5, DRINK_2_INGREDIENT_6, DRINK_2_INGREDIENT_7);
    }
}