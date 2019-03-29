package com.ezgroceries.shoppinglist.service;

import com.ezgroceries.shoppinglist.client.CocktailDBClient;
import com.ezgroceries.shoppinglist.contract.cocktaildb.CocktailDBResponseResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CocktailDBService {

    private final CocktailDBClient cocktailDBClient;

    public CocktailDBService(CocktailDBClient cocktailDBClient) {
        this.cocktailDBClient = cocktailDBClient;
    }

    public ResponseEntity<CocktailDBResponseResource> getCocktailBySearchTerm(String search) {
        return cocktailDBClient.getCocktailBySearchTerm(search);
    }
}
