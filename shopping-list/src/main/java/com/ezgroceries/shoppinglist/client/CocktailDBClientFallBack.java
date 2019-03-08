package com.ezgroceries.shoppinglist.client;

import com.ezgroceries.shoppinglist.contract.cocktaildb.CocktailDBResponseResource;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CocktailDBClientFallBack implements CocktailDBClient {

    private final Throwable cause;

    public CocktailDBClientFallBack(Throwable cause) {
        this.cause = cause;
    }

    @Override
    public ResponseEntity<CocktailDBResponseResource> getCocktailBySearchTerm(String searchTerm) {
        if (cause instanceof FeignException && ((FeignException) cause).status() != HttpStatus.OK.value()) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
