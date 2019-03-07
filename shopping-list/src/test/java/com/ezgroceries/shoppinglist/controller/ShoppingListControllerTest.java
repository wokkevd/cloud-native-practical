package com.ezgroceries.shoppinglist.controller;

import com.ezgroceries.shoppinglist.contract.CocktailResource;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = ShoppingListController.class, secure = false)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ShoppingListControllerTest {

    private static final UUID ADDED_COCKTAIL_ID = UUID.randomUUID();
    private static final UUID FAKE_COCKTAIL_ID = UUID.randomUUID();
    private static final String POST_LIST_NAME = "Test List";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private CocktailController cocktailController;

    @Test
    public void test1getShoppingLists() throws Exception {
        mockMvc.perform(get("/shopping-lists")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.shoppingListResourceList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.shoppingListResourceList.[0].shoppingListId", is("eb18bb7c-61f3-4c9f-981c-55b1b8ee8915")))
                .andExpect(jsonPath("$._embedded.shoppingListResourceList.[0].name", is("Stephanie's birthday")))
                .andExpect(jsonPath("$._embedded.shoppingListResourceList.[0].ingredients", hasSize(5)))
                .andExpect(jsonPath("$._embedded.shoppingListResourceList.[0].ingredients.[0]", is("Tequila")))
                .andExpect(jsonPath("$._embedded.shoppingListResourceList.[0].ingredients.[1]", is("Triple sec")))
                .andExpect(jsonPath("$._embedded.shoppingListResourceList.[0].ingredients.[2]", is("Lime juice")))
                .andExpect(jsonPath("$._embedded.shoppingListResourceList.[0].ingredients.[3]", is("Salt")))
                .andExpect(jsonPath("$._embedded.shoppingListResourceList.[0].ingredients.[4]", is("Blue Curacao")));
    }

    @Test
    public void test2getShoppingList() throws Exception {
        mockMvc.perform(get("/shopping-lists/eb18bb7c-61f3-4c9f-981c-55b1b8ee8915")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shoppingListId", is("eb18bb7c-61f3-4c9f-981c-55b1b8ee8915")))
                .andExpect(jsonPath("$.name", is("Stephanie's birthday")))
                .andExpect(jsonPath("$.ingredients", hasSize(5)))
                .andExpect(jsonPath("$.ingredients.[0]", is("Tequila")))
                .andExpect(jsonPath("$.ingredients.[1]", is("Triple sec")))
                .andExpect(jsonPath("$.ingredients.[2]", is("Lime juice")))
                .andExpect(jsonPath("$.ingredients.[3]", is("Salt")))
                .andExpect(jsonPath("$.ingredients.[4]", is("Blue Curacao")));
    }

    @Test
    public void test3getNonExistingShoppingList() throws Exception {
        Optional<Exception> resolvedException = Optional.ofNullable(mockMvc.perform(get("/shopping-lists/d615ec78-fe93-467b-8d26-5d26d8eab073")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn().getResolvedException());

        assertThat(resolvedException).isPresent();
        resolvedException.ifPresent(e -> assertThat(e.getMessage()).isEqualTo("Shopping list with id d615ec78-fe93-467b-8d26-5d26d8eab073 not found"));
    }

    @Test
    public void test4addCocktailsToList() throws Exception {
        List<ShoppingListController.CocktailIdResource> cocktailIdResource = Collections.singletonList(new ShoppingListController.CocktailIdResource(ADDED_COCKTAIL_ID));
        CocktailResource mocktail = mock(CocktailResource.class);
        when(mocktail.getCocktailId()).thenReturn(ADDED_COCKTAIL_ID);
        when(mocktail.getIngredients()).thenReturn(Arrays.asList("Water", "Grenadine"));
        when(cocktailController.getCocktails()).thenReturn(new Resources<>(Collections.singletonList(mocktail)));

        mockMvc.perform(post("/shopping-lists/eb18bb7c-61f3-4c9f-981c-55b1b8ee8915/cocktails")
                .content(objectMapper.writeValueAsString(cocktailIdResource))
                .contentType(MediaType.APPLICATION_JSON))
                // THEN
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.cocktailIdResourceList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.cocktailIdResourceList.[0].cocktailId", is(ADDED_COCKTAIL_ID.toString())));

        mockMvc.perform(get("/shopping-lists/eb18bb7c-61f3-4c9f-981c-55b1b8ee8915")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shoppingListId", is("eb18bb7c-61f3-4c9f-981c-55b1b8ee8915")))
                .andExpect(jsonPath("$.name", is("Stephanie's birthday")))
                .andExpect(jsonPath("$.ingredients", hasSize(7)))
                .andExpect(jsonPath("$.ingredients.[0]", is("Tequila")))
                .andExpect(jsonPath("$.ingredients.[1]", is("Triple sec")))
                .andExpect(jsonPath("$.ingredients.[2]", is("Lime juice")))
                .andExpect(jsonPath("$.ingredients.[3]", is("Salt")))
                .andExpect(jsonPath("$.ingredients.[4]", is("Blue Curacao")))
                .andExpect(jsonPath("$.ingredients.[5]", is("Water")))
                .andExpect(jsonPath("$.ingredients.[6]", is("Grenadine")));
    }

    @Test
    public void test5addCocktailsToNonExistingList() throws Exception {
        List<ShoppingListController.CocktailIdResource> cocktailIdResource = Collections.singletonList(new ShoppingListController.CocktailIdResource(ADDED_COCKTAIL_ID));

        Optional<Exception> resolvedException = Optional.ofNullable(mockMvc.perform(post("/shopping-lists/d615ec78-fe93-467b-8d26-5d26d8eab073/cocktails")
                .content(objectMapper.writeValueAsString(cocktailIdResource))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn().getResolvedException());

        assertThat(resolvedException).isPresent();
        resolvedException.ifPresent(e -> assertThat(e.getMessage()).isEqualTo("Shopping list with id d615ec78-fe93-467b-8d26-5d26d8eab073 not found"));
    }

    @Test
    public void test6addNonExistingCocktailsToList() throws Exception {
        List<ShoppingListController.CocktailIdResource> cocktailIdResource =
                Collections.singletonList(new ShoppingListController.CocktailIdResource(FAKE_COCKTAIL_ID));
        CocktailResource mocktail = mock(CocktailResource.class);
        when(mocktail.getCocktailId()).thenReturn(ADDED_COCKTAIL_ID);
        when(cocktailController.getCocktails()).thenReturn(new Resources<>(Collections.singletonList(mocktail)));

        mockMvc.perform(post("/shopping-lists/eb18bb7c-61f3-4c9f-981c-55b1b8ee8915/cocktails")
                .content(objectMapper.writeValueAsString(cocktailIdResource))
                .contentType(MediaType.APPLICATION_JSON))
                // THEN
                .andExpect(status().isOk())
                .andExpect(content().string("{}"));

        mockMvc.perform(get("/shopping-lists/eb18bb7c-61f3-4c9f-981c-55b1b8ee8915")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shoppingListId", is("eb18bb7c-61f3-4c9f-981c-55b1b8ee8915")))
                .andExpect(jsonPath("$.name", is("Stephanie's birthday")))
                .andExpect(jsonPath("$.ingredients", hasSize(7)))
                .andExpect(jsonPath("$.ingredients.[0]", is("Tequila")))
                .andExpect(jsonPath("$.ingredients.[1]", is("Triple sec")))
                .andExpect(jsonPath("$.ingredients.[2]", is("Lime juice")))
                .andExpect(jsonPath("$.ingredients.[3]", is("Salt")))
                .andExpect(jsonPath("$.ingredients.[4]", is("Blue Curacao")))
                .andExpect(jsonPath("$.ingredients.[5]", is("Water")))
                .andExpect(jsonPath("$.ingredients.[6]", is("Grenadine")));
    }

    @Test
    public void test7createShoppingList() throws Exception {
        ShoppingListController.ShoppingListCreationResource postResource = new ShoppingListController.ShoppingListCreationResource(POST_LIST_NAME);

        mockMvc.perform(post("/shopping-lists")
                .content(objectMapper.writeValueAsString(postResource))
                .contentType(MediaType.APPLICATION_JSON))
                // THEN
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.shoppingListId", not(isEmptyOrNullString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is(POST_LIST_NAME)));
    }
}