package com.ezgroceries.shoppinglist.repository;

import com.ezgroceries.shoppinglist.annotation.DBUnitTest;
import com.ezgroceries.shoppinglist.domain.CocktailEntity;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DBUnitTest
@DatabaseSetup({CocktailRepositoryTest.DATASET})
@DatabaseTearDown(type = DatabaseOperation.DELETE, value = {CocktailRepositoryTest.DATASET})
public class CocktailRepositoryTest {

    static final String DATASET = "classpath:com/ezgroceries/shoppinglist/cocktails_dataset.xml";

    private static final UUID COCKTAIL_UUID = UUID.fromString("a1fd2e4e-4f49-5d7c-adce-37dd0d8395e7");
    private static final String DRINK_ID = "123456";
    private static final String DRINK_NAME = "Margerita";
    private static final String GLASS = "Cocktail Glass";
    private static final String DRINK_INSTRUCTIONS = "Rub the rim of the glass with the lime slice to make the salt stick to it. Take care to moisten..";
    private static final String DRINK_URL = "https://www.thecocktaildb.com/images/media/drink/wpxpvu1439905379.jpg";

    @Autowired
    private CocktailRepository cocktailRepository;

    @Test
    public void findByExistingDrinkId() {
        Optional<CocktailEntity> foundDrink = cocktailRepository.findByDrinkId("123456");
        assertThat(foundDrink).isPresent();
        assertThat(foundDrink.get().getId()).isEqualTo(COCKTAIL_UUID);
        assertThat(foundDrink.get().getDrinkId()).isEqualTo(DRINK_ID);
        assertThat(foundDrink.get().getName()).isEqualTo(DRINK_NAME);
        assertThat(foundDrink.get().getGlass()).isEqualTo(GLASS);
        assertThat(foundDrink.get().getInstructions()).isEqualTo(DRINK_INSTRUCTIONS);
        assertThat(foundDrink.get().getUrl()).isEqualTo(DRINK_URL);
        assertThat(foundDrink.get().getIngredients()).containsExactly("Tequila", "Triple sec", "Lime juice", "Salt");
    }

    @Test
    public void findByNonExistingDrinkId() {
        Optional<CocktailEntity> optionalCocktail = cocktailRepository.findByDrinkId("DUNNO");
        assertThat(optionalCocktail).isNotPresent();
    }

}