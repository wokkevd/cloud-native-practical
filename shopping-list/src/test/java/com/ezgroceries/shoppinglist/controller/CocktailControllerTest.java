package com.ezgroceries.shoppinglist.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = CocktailController.class, secure = false)
public class CocktailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getCocktails() throws Exception {
        mockMvc.perform(get("/cocktails")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.cocktailResourceList", hasSize(2)))
                .andExpect(jsonPath("$._embedded.cocktailResourceList.[0].cocktailId", is("23b3d85a-3928-41c0-a533-6538a71e17c4")))
                .andExpect(jsonPath("$._embedded.cocktailResourceList.[0].name", is("Margerita")))
                .andExpect(jsonPath("$._embedded.cocktailResourceList.[0].glass", is("Cocktail glass")))
                .andExpect(jsonPath("$._embedded.cocktailResourceList.[0].instructions", is("Rub the rim of the glass with the lime slice to make the salt stick to it. Take care to moisten..")))
                .andExpect(jsonPath("$._embedded.cocktailResourceList.[0].image", is("https://www.thecocktaildb.com/images/media/drink/wpxpvu1439905379.jpg")))
                .andExpect(jsonPath("$._embedded.cocktailResourceList.[0].ingredients", hasSize(4)))
                .andExpect(jsonPath("$._embedded.cocktailResourceList.[0].ingredients.[0]", is("Tequila")))
                .andExpect(jsonPath("$._embedded.cocktailResourceList.[0].ingredients.[1]", is("Triple sec")))
                .andExpect(jsonPath("$._embedded.cocktailResourceList.[0].ingredients.[2]", is("Lime juice")))
                .andExpect(jsonPath("$._embedded.cocktailResourceList.[0].ingredients.[3]", is("Salt")))
                .andExpect(jsonPath("$._embedded.cocktailResourceList.[1].cocktailId", is("d615ec78-fe93-467b-8d26-5d26d8eab073")))
                .andExpect(jsonPath("$._embedded.cocktailResourceList.[1].name", is("Blue Margerita")))
                .andExpect(jsonPath("$._embedded.cocktailResourceList.[1].glass", is("Cocktail glass")))
                .andExpect(jsonPath("$._embedded.cocktailResourceList.[1].instructions", is("Rub rim of cocktail glass with lime juice. Dip rim in coarse salt..")))
                .andExpect(jsonPath("$._embedded.cocktailResourceList.[1].image", is("https://www.thecocktaildb.com/images/media/drink/qtvvyq1439905913.jpg")))
                .andExpect(jsonPath("$._embedded.cocktailResourceList.[1].ingredients", hasSize(4)))
                .andExpect(jsonPath("$._embedded.cocktailResourceList.[1].ingredients.[0]", is("Tequila")))
                .andExpect(jsonPath("$._embedded.cocktailResourceList.[1].ingredients.[1]", is("Blue Curacao")))
                .andExpect(jsonPath("$._embedded.cocktailResourceList.[1].ingredients.[2]", is("Lime juice")))
                .andExpect(jsonPath("$._embedded.cocktailResourceList.[1].ingredients.[3]", is("Salt")));
    }

    @Test
    public void getCocktailsWithSearch() throws Exception {
        mockMvc.perform(get("/cocktails?search=Blue")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.cocktailResourceList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.cocktailResourceList.[0].cocktailId", is("d615ec78-fe93-467b-8d26-5d26d8eab073")))
                .andExpect(jsonPath("$._embedded.cocktailResourceList.[0].name", is("Blue Margerita")))
                .andExpect(jsonPath("$._embedded.cocktailResourceList.[0].glass", is("Cocktail glass")))
                .andExpect(jsonPath("$._embedded.cocktailResourceList.[0].instructions", is("Rub rim of cocktail glass with lime juice. Dip rim in coarse salt..")))
                .andExpect(jsonPath("$._embedded.cocktailResourceList.[0].image", is("https://www.thecocktaildb.com/images/media/drink/qtvvyq1439905913.jpg")))
                .andExpect(jsonPath("$._embedded.cocktailResourceList.[0].ingredients", hasSize(4)))
                .andExpect(jsonPath("$._embedded.cocktailResourceList.[0].ingredients.[0]", is("Tequila")))
                .andExpect(jsonPath("$._embedded.cocktailResourceList.[0].ingredients.[1]", is("Blue Curacao")))
                .andExpect(jsonPath("$._embedded.cocktailResourceList.[0].ingredients.[2]", is("Lime juice")))
                .andExpect(jsonPath("$._embedded.cocktailResourceList.[0].ingredients.[3]", is("Salt")));
    }

    @Test
    public void getCocktailsWithNonExistingSearch() throws Exception {
        mockMvc.perform(get("/cocktails?search=Black")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("{}"));
    }

    @Test
    public void getCocktail() throws Exception {
        mockMvc.perform(get("/cocktails/Margerita")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cocktailId", is("23b3d85a-3928-41c0-a533-6538a71e17c4")))
                .andExpect(jsonPath("$.name", is("Margerita")))
                .andExpect(jsonPath("$.glass", is("Cocktail glass")))
                .andExpect(jsonPath("$.instructions", is("Rub the rim of the glass with the lime slice to make the salt stick to it. Take care to moisten..")))
                .andExpect(jsonPath("$.image", is("https://www.thecocktaildb.com/images/media/drink/wpxpvu1439905379.jpg")))
                .andExpect(jsonPath("$.ingredients", hasSize(4)))
                .andExpect(jsonPath("$.ingredients.[0]", is("Tequila")))
                .andExpect(jsonPath("$.ingredients.[1]", is("Triple sec")))
                .andExpect(jsonPath("$.ingredients.[2]", is("Lime juice")))
                .andExpect(jsonPath("$.ingredients.[3]", is("Salt")));
    }

    @Test
    public void getNonExistingCocktail() throws Exception {
        Optional<Exception> resolvedException = Optional.ofNullable(mockMvc.perform(get("/cocktails/White Russian")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn().getResolvedException());

        assertThat(resolvedException).isPresent();
        resolvedException.ifPresent(e -> assertThat(e.getMessage()).isEqualTo("Cocktail with name White Russian not found"));
    }
}