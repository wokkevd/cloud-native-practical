package com.ezgroceries.shoppinglist.controller;

import com.ezgroceries.shoppinglist.client.CocktailDBClient;
import com.ezgroceries.shoppinglist.contract.cocktaildb.CocktailDBDrinkResource;
import com.ezgroceries.shoppinglist.contract.cocktaildb.CocktailDBResponseResource;
import com.ezgroceries.shoppinglist.contract.shoppinglist.CocktailResource;
import com.ezgroceries.shoppinglist.service.CocktailService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = CocktailController.class, secure = false)
public class CocktailControllerTest {

    private static final UUID EXISTING_COCKTAIL_ID_1 = UUID.randomUUID();
    private static final UUID EXISTING_COCKTAIL_ID_2 = UUID.randomUUID();
    private static final UUID UNKNOWN_COCKTAIL_ID = UUID.randomUUID();
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
    private MockMvc mockMvc;
    @MockBean
    private CocktailDBClient cocktailDBClient;
    @MockBean
    private CocktailService cocktailService;

    private CocktailResource cocktailResource1;
    private CocktailResource cocktailResource2;
    private CocktailDBDrinkResource drinkResource1;
    private CocktailDBDrinkResource drinkResource2;

    @Before
    public void setUp() {
        cocktailResource1 = CocktailResource.builder()
                .id(EXISTING_COCKTAIL_ID_1)
                .cocktailId(DRINK_ID_1)
                .name(DRINK_NAME_1)
                .glass(GLASS)
                .instructions(DRINK_INSTRUCTIONS_1)
                .image(DRINK_URL_1)
                .ingredients(Arrays.asList("Tequila", "Triple sec", "Lime juice", "Salt"))
                .build();
        cocktailResource2 = CocktailResource.builder()
                .id(EXISTING_COCKTAIL_ID_2)
                .cocktailId(DRINK_ID_2)
                .name(DRINK_NAME_2)
                .glass(GLASS)
                .instructions(DRINK_INSTRUCTIONS_2)
                .image(DRINK_URL_2)
                .ingredients(Arrays.asList("Tequila", "Blue Curacao", "Lime juice", "Salt"))
                .build();
        drinkResource1 = new CocktailDBDrinkResource(
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
        drinkResource2 = new CocktailDBDrinkResource(
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
    }

    @Test
    public void getCocktails() throws Exception {
        List<CocktailDBDrinkResource> drinks = Arrays.asList(drinkResource1, drinkResource2);
        when(cocktailDBClient.getCocktailBySearchTerm(DRINK_NAME_1)).thenReturn(ResponseEntity.ok().body(new CocktailDBResponseResource(drinks)));
        when(cocktailService.persistCocktails(drinks)).thenReturn(Arrays.asList(cocktailResource1, cocktailResource2));

        mockMvc.perform(get("/cocktails?search=Margerita")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id", is(EXISTING_COCKTAIL_ID_1.toString())))
                .andExpect(jsonPath("$.[0].cocktailId", is(DRINK_ID_1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name", is(DRINK_NAME_1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].glass", is(GLASS)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].instructions", is(DRINK_INSTRUCTIONS_1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].image", is(DRINK_URL_1)))
                .andExpect(jsonPath("$.[0].ingredients", hasSize(4)))
                .andExpect(jsonPath("$.[0].ingredients.[0]", is("Tequila")))
                .andExpect(jsonPath("$.[0].ingredients.[1]", is("Triple sec")))
                .andExpect(jsonPath("$.[0].ingredients.[2]", is("Lime juice")))
                .andExpect(jsonPath("$.[0].ingredients.[3]", is("Salt")))
                .andExpect(jsonPath("$.[1].id", is(EXISTING_COCKTAIL_ID_2.toString())))
                .andExpect(jsonPath("$.[1].cocktailId", is(DRINK_ID_2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].name", is(DRINK_NAME_2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].glass", is(GLASS)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].instructions", is(DRINK_INSTRUCTIONS_2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].image", is(DRINK_URL_2)))
                .andExpect(jsonPath("$.[1].ingredients", hasSize(4)))
                .andExpect(jsonPath("$.[1].ingredients.[0]", is("Tequila")))
                .andExpect(jsonPath("$.[1].ingredients.[1]", is("Blue Curacao")))
                .andExpect(jsonPath("$.[1].ingredients.[2]", is("Lime juice")))
                .andExpect(jsonPath("$.[1].ingredients.[3]", is("Salt")));
    }

    @Test
    public void getCocktailsWithNonExistingSearch() throws Exception {
        when(cocktailDBClient.getCocktailBySearchTerm("Fart")).thenReturn(ResponseEntity.ok().body(new CocktailDBResponseResource(new ArrayList<>())));
        when(cocktailService.persistCocktails(new ArrayList<>())).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/cocktails?search=Fart")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    public void getCocktail() throws Exception {
        when(cocktailService.findById(EXISTING_COCKTAIL_ID_1)).thenReturn(cocktailResource1);
        mockMvc.perform(get("/cocktails/" + EXISTING_COCKTAIL_ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(EXISTING_COCKTAIL_ID_1.toString())))
                .andExpect(jsonPath("$.cocktailId", is(DRINK_ID_1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is(DRINK_NAME_1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.glass", is(GLASS)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.instructions", is(DRINK_INSTRUCTIONS_1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.image", is(DRINK_URL_1)))
                .andExpect(jsonPath("$.ingredients", hasSize(4)))
                .andExpect(jsonPath("$.ingredients.[0]", is("Tequila")))
                .andExpect(jsonPath("$.ingredients.[1]", is("Triple sec")))
                .andExpect(jsonPath("$.ingredients.[2]", is("Lime juice")))
                .andExpect(jsonPath("$.ingredients.[3]", is("Salt")));
    }

    @Test
    public void getNonExistingCocktail() throws Exception {
        Optional<Exception> resolvedException = Optional.ofNullable(mockMvc.perform(get("/cocktails/" + UNKNOWN_COCKTAIL_ID.toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn().getResolvedException());

        assertThat(resolvedException).isPresent();
        resolvedException.ifPresent(e -> assertThat(e.getMessage()).isEqualTo("Cocktail with id " + UNKNOWN_COCKTAIL_ID.toString() + " not found"));
    }
}