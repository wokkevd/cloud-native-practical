package com.ezgroceries.shoppinglist.service;

import com.ezgroceries.shoppinglist.contract.cocktaildb.CocktailDBDrinkResource;
import com.ezgroceries.shoppinglist.contract.shoppinglist.CocktailResource;
import com.ezgroceries.shoppinglist.domain.CocktailEntity;
import com.ezgroceries.shoppinglist.factory.CocktailEntityFactory;
import com.ezgroceries.shoppinglist.factory.CocktailResourceFactory;
import com.ezgroceries.shoppinglist.repository.CocktailRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CocktailServiceTest {

    private static final UUID EXISTING_COCKTAIL_ID_1 = UUID.randomUUID();
    private static final UUID EXISTING_COCKTAIL_ID_2 = UUID.randomUUID();
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
    private CocktailService cocktailService;
    @MockBean
    private CocktailRepository cocktailRepository;
    @SpyBean
    private CocktailResourceFactory cocktailResourceFactory;
    @SpyBean
    private CocktailEntityFactory cocktailEntityFactory;
    @Captor
    private ArgumentCaptor<CocktailEntity> cocktailEntityArgumentCaptor;

    @Test
    public void persistCocktails() {
        List<String> ingredients1 = Arrays.asList("Tequila", "Triple sec", "Lime juice", "Salt");
        CocktailEntity cocktailEntity1 = CocktailEntity.builder()
                .drinkId(DRINK_ID_1)
                .name(DRINK_NAME_1)
                .glass(GLASS)
                .instructions(DRINK_INSTRUCTIONS_1)
                .url(DRINK_URL_1)
                .ingredients(ingredients1)
                .build();
        cocktailEntity1.setId(EXISTING_COCKTAIL_ID_1);
        List<String> ingredients2 = Arrays.asList("Tequila", "Blue Curacao", "Lime juice", "Salt");
        CocktailEntity cocktailEntity2 = CocktailEntity.builder()
                .drinkId(DRINK_ID_2)
                .name(DRINK_NAME_2)
                .glass(GLASS)
                .instructions(DRINK_INSTRUCTIONS_2)
                .url(DRINK_URL_2)
                .ingredients(ingredients2)
                .build();
        cocktailEntity2.setId(EXISTING_COCKTAIL_ID_2);
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

        when(cocktailRepository.findByDrinkId("123")).thenReturn(Optional.of(cocktailEntity1));
        when(cocktailRepository.findByDrinkId("456")).thenReturn(Optional.empty());
        when(cocktailRepository.save(cocktailEntityArgumentCaptor.capture())).thenReturn(cocktailEntity2);
        when(cocktailEntityFactory.create(drinkResource2)).thenReturn(cocktailEntity2);
        List<CocktailResource> cocktailResources = cocktailService.persistCocktails(Arrays.asList(drinkResource1, drinkResource2));
        assertThat(cocktailResources).isNotEmpty();
        assertThat(cocktailResources).hasSize(2);
        assertThat(cocktailResources.get(0).getId()).isEqualTo(EXISTING_COCKTAIL_ID_1);
        assertThat(cocktailResources.get(0).getCocktailId()).isEqualTo(DRINK_ID_1);
        assertThat(cocktailResources.get(0).getName()).isEqualTo(DRINK_NAME_1);
        assertThat(cocktailResources.get(0).getGlass()).isEqualTo(GLASS);
        assertThat(cocktailResources.get(0).getInstructions()).isEqualTo(DRINK_INSTRUCTIONS_1);
        assertThat(cocktailResources.get(0).getImage()).isEqualTo(DRINK_URL_1);
        assertThat(cocktailResources.get(0).getIngredients()).isEqualTo(ingredients1);
        assertThat(cocktailResources.get(1).getId()).isEqualTo(EXISTING_COCKTAIL_ID_2);
        assertThat(cocktailResources.get(1).getCocktailId()).isEqualTo(DRINK_ID_2);
        assertThat(cocktailResources.get(1).getName()).isEqualTo(DRINK_NAME_2);
        assertThat(cocktailResources.get(1).getGlass()).isEqualTo(GLASS);
        assertThat(cocktailResources.get(1).getInstructions()).isEqualTo(DRINK_INSTRUCTIONS_2);
        assertThat(cocktailResources.get(1).getImage()).isEqualTo(DRINK_URL_2);
        assertThat(cocktailResources.get(1).getIngredients()).isEqualTo(ingredients2);
        List<CocktailEntity> savedEntities = cocktailEntityArgumentCaptor.getAllValues();
        assertThat(savedEntities).hasSize(1);
        assertThat(savedEntities).containsExactly(cocktailEntity2);
    }

    @Test
    public void findById() {
        List<String> ingredients = Arrays.asList("Tequila", "Triple sec", "Lime juice", "Salt");
        CocktailEntity cocktailEntity1 = CocktailEntity.builder()
                .drinkId(DRINK_ID_1)
                .name(DRINK_NAME_1)
                .glass(GLASS)
                .instructions(DRINK_INSTRUCTIONS_1)
                .url(DRINK_URL_1)
                .ingredients(ingredients)
                .build();
        cocktailEntity1.setId(EXISTING_COCKTAIL_ID_1);
        when(cocktailRepository.findById(EXISTING_COCKTAIL_ID_1)).thenReturn(Optional.of(cocktailEntity1));
        CocktailResource foundCocktail = cocktailService.findById(EXISTING_COCKTAIL_ID_1);
        assertThat(foundCocktail).isNotNull();
        assertThat(foundCocktail.getId()).isEqualTo(EXISTING_COCKTAIL_ID_1);
        assertThat(foundCocktail.getCocktailId()).isEqualTo(DRINK_ID_1);
        assertThat(foundCocktail.getName()).isEqualTo(DRINK_NAME_1);
        assertThat(foundCocktail.getGlass()).isEqualTo(GLASS);
        assertThat(foundCocktail.getInstructions()).isEqualTo(DRINK_INSTRUCTIONS_1);
        assertThat(foundCocktail.getImage()).isEqualTo(DRINK_URL_1);
        assertThat(foundCocktail.getIngredients()).isEqualTo(ingredients);
    }
}