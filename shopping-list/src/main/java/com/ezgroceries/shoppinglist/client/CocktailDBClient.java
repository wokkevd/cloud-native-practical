package com.ezgroceries.shoppinglist.client;

import com.ezgroceries.shoppinglist.contract.cocktaildb.CocktailDBResponseResource;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "cocktaildb-client", url = "${cocktaildb.api-url}", fallbackFactory = CocktailDBClientFallBackFactory.class)
@Component
public interface CocktailDBClient {

    @GetMapping(value = "/search.php")
    ResponseEntity<CocktailDBResponseResource> getCocktailBySearchTerm(@RequestParam("s") String searchTerm);
}
