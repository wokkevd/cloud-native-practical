package com.ezgroceries.shoppinglist.controller;

import com.ezgroceries.shoppinglist.contract.shoppinglist.ShoppingListResource;
import com.ezgroceries.shoppinglist.exceptions.NotFoundException;
import com.ezgroceries.shoppinglist.service.ShoppingListService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = ShoppingListController.class, secure = false)
public class ShoppingListControllerTest {

    private static final UUID ADDED_COCKTAIL_ID = UUID.randomUUID();
    private static final UUID FAKE_COCKTAIL_ID = UUID.randomUUID();
    private static final String POST_LIST_NAME = "Test List";
    private static final UUID SHOPPING_LIST_ID_1 = UUID.randomUUID();
    private static final UUID SHOPPING_LIST_ID_2 = UUID.randomUUID();
    private static final UUID SHOPPING_LIST_ID_X = UUID.randomUUID();
    private static final UUID SHOPPING_LIST_ID_POSTED = UUID.randomUUID();
    private static final String SHOPPING_LIST_NAME_1 = "Stephanie's birthday";
    private static final String SHOPPING_LIST_NAME_2 = "Nick's birthday";
    private static final List<String> INGREDIENTS_1 = asList("Tequila", "Triple sec", "Lime juice", "Salt", "Blue Curacao");
    private static final List<String> INGREDIENTS_2 = asList("Vodka", "Coffee liqueur", "Light cream");

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ShoppingListService shoppingListService;

    @Before
    public void setUp() {
        ShoppingListResource shoppingList1 = new ShoppingListResource(SHOPPING_LIST_ID_1, SHOPPING_LIST_NAME_1);
        shoppingList1.setIngredients(INGREDIENTS_1);
        ShoppingListResource shoppingList2 = new ShoppingListResource(SHOPPING_LIST_ID_2, SHOPPING_LIST_NAME_2);
        shoppingList2.setIngredients(INGREDIENTS_2);
        ShoppingListResource shoppingListPosted = new ShoppingListResource(SHOPPING_LIST_ID_POSTED, POST_LIST_NAME);
        when(shoppingListService.getAllShoppingLists()).thenReturn(asList(shoppingList1, shoppingList2));
        when(shoppingListService.getShoppingList(SHOPPING_LIST_ID_1)).thenReturn(shoppingList1);
        when(shoppingListService.getShoppingList(SHOPPING_LIST_ID_2)).thenReturn(shoppingList2);
        when(shoppingListService.addCocktails(SHOPPING_LIST_ID_1, singletonList(ADDED_COCKTAIL_ID))).thenReturn(singletonList(ADDED_COCKTAIL_ID));
        when(shoppingListService.addCocktails(SHOPPING_LIST_ID_1, singletonList(FAKE_COCKTAIL_ID))).thenReturn(emptyList());
        when(shoppingListService.addCocktails(eq(SHOPPING_LIST_ID_X), any())).thenThrow(new NotFoundException("Shopping list with id " + SHOPPING_LIST_ID_X.toString() + " not found"));
        when(shoppingListService.createShoppingList(POST_LIST_NAME)).thenReturn(shoppingListPosted);
    }

    @Test
    public void getShoppingLists() throws Exception {
        setUp();
        mockMvc.perform(get("/shopping-lists")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].shoppingListId", is(SHOPPING_LIST_ID_1.toString())))
                .andExpect(jsonPath("$.[0].name", is(SHOPPING_LIST_NAME_1)))
                .andExpect(jsonPath("$.[0].ingredients", hasSize(INGREDIENTS_1.size())))
                .andExpect(jsonPath("$.[0].ingredients.[0]", is(INGREDIENTS_1.get(0))))
                .andExpect(jsonPath("$.[0].ingredients.[1]", is(INGREDIENTS_1.get(1))))
                .andExpect(jsonPath("$.[0].ingredients.[2]", is(INGREDIENTS_1.get(2))))
                .andExpect(jsonPath("$.[0].ingredients.[3]", is(INGREDIENTS_1.get(3))))
                .andExpect(jsonPath("$.[0].ingredients.[4]", is(INGREDIENTS_1.get(4))))
                .andExpect(jsonPath("$.[1].shoppingListId", is(SHOPPING_LIST_ID_2.toString())))
                .andExpect(jsonPath("$.[1].name", is(SHOPPING_LIST_NAME_2)))
                .andExpect(jsonPath("$.[1].ingredients", hasSize(INGREDIENTS_2.size())))
                .andExpect(jsonPath("$.[1].ingredients.[0]", is(INGREDIENTS_2.get(0))))
                .andExpect(jsonPath("$.[1].ingredients.[1]", is(INGREDIENTS_2.get(1))))
                .andExpect(jsonPath("$.[1].ingredients.[2]", is(INGREDIENTS_2.get(2))));
    }

    @Test
    public void getShoppingList() throws Exception {
        mockMvc.perform(get("/shopping-lists/" + SHOPPING_LIST_ID_1.toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shoppingListId", is(SHOPPING_LIST_ID_1.toString())))
                .andExpect(jsonPath("$.name", is(SHOPPING_LIST_NAME_1)))
                .andExpect(jsonPath("$.ingredients", hasSize(INGREDIENTS_1.size())))
                .andExpect(jsonPath("$.ingredients.[0]", is(INGREDIENTS_1.get(0))))
                .andExpect(jsonPath("$.ingredients.[1]", is(INGREDIENTS_1.get(1))))
                .andExpect(jsonPath("$.ingredients.[2]", is(INGREDIENTS_1.get(2))))
                .andExpect(jsonPath("$.ingredients.[3]", is(INGREDIENTS_1.get(3))))
                .andExpect(jsonPath("$.ingredients.[4]", is(INGREDIENTS_1.get(4))));
    }

    @Test
    public void getNonExistingShoppingList() throws Exception {
        Optional<Exception> resolvedException = Optional.ofNullable(mockMvc.perform(get("/shopping-lists/" + SHOPPING_LIST_ID_X.toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn().getResolvedException());

        assertThat(resolvedException).isPresent();
        resolvedException.ifPresent(e -> assertThat(e.getMessage()).isEqualTo("Shopping list with id " + SHOPPING_LIST_ID_X.toString() + " not found"));
    }

    @Test
    public void addCocktailsToList() throws Exception {
        List<ShoppingListController.CocktailIdResource> cocktailIdResource = Collections.singletonList(new ShoppingListController.CocktailIdResource(ADDED_COCKTAIL_ID));
        mockMvc.perform(post("/shopping-lists/" + SHOPPING_LIST_ID_1.toString() + "/cocktails")
                .content(objectMapper.writeValueAsString(cocktailIdResource))
                .contentType(MediaType.APPLICATION_JSON))
                // THEN
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.cocktailIdResourceList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.cocktailIdResourceList.[0].id", is(ADDED_COCKTAIL_ID.toString())));
    }

    @Test
    public void addCocktailsToNonExistingList() throws Exception {
        List<ShoppingListController.CocktailIdResource> cocktailIdResource = singletonList(new ShoppingListController.CocktailIdResource(ADDED_COCKTAIL_ID));
        Optional<Exception> resolvedException = Optional.ofNullable(
                mockMvc.perform(post("/shopping-lists/" + SHOPPING_LIST_ID_X.toString() + "/cocktails")
                        .content(objectMapper.writeValueAsString(cocktailIdResource))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound())
                        .andReturn().getResolvedException());

        assertThat(resolvedException).isPresent();
        resolvedException.ifPresent(e -> assertThat(e.getMessage()).isEqualTo("Shopping list with id " + SHOPPING_LIST_ID_X.toString() + " not found"));
    }

    @Test
    public void addNonExistingCocktailsToList() throws Exception {
        List<ShoppingListController.CocktailIdResource> cocktailIdResource = singletonList(new ShoppingListController.CocktailIdResource(FAKE_COCKTAIL_ID));
        mockMvc.perform(post("/shopping-lists/" + SHOPPING_LIST_ID_1.toString() + "/cocktails")
                .content(objectMapper.writeValueAsString(cocktailIdResource))
                .contentType(MediaType.APPLICATION_JSON))
                // THEN
                .andExpect(status().isOk())
                .andExpect(content().string("{}"));
    }

    @Test
    public void createShoppingList() throws Exception {
        ShoppingListController.ShoppingListCreationResource postResource = new ShoppingListController.ShoppingListCreationResource(POST_LIST_NAME);
        mockMvc.perform(post("/shopping-lists")
                .content(objectMapper.writeValueAsString(postResource))
                .contentType(MediaType.APPLICATION_JSON))
                // THEN
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.shoppingListId", is(SHOPPING_LIST_ID_POSTED.toString())))
                .andExpect(jsonPath("$.name", is(POST_LIST_NAME)));
    }
}