package com.ezgroceries.shoppinglist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = ShoppingListController.class, secure = false)
public class ShoppingListController3CreateTest {

    private static final String POST_LIST_NAME = "Test List";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private CocktailController cocktailController;

    @Test
    public void createShoppingList() throws Exception {
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