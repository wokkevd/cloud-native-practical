package com.ezgroceries.shoppinglist.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = ShoppingListController.class, secure = false)
public class ShoppingListController1GetTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CocktailController cocktailController;

    @Test
    public void getShoppingLists() throws Exception {
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
    public void getShoppingList() throws Exception {
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
    public void getNonExistingShoppingList() throws Exception {
        Optional<Exception> resolvedException = Optional.ofNullable(mockMvc.perform(get("/shopping-lists/d615ec78-fe93-467b-8d26-5d26d8eab073")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn().getResolvedException());

        assertThat(resolvedException).isPresent();
        resolvedException.ifPresent(e -> assertThat(e.getMessage()).isEqualTo("Shopping list with id d615ec78-fe93-467b-8d26-5d26d8eab073 not found"));
    }
}