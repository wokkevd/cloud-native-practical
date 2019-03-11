package com.ezgroceries.shoppinglist.service;

import com.ezgroceries.shoppinglist.contract.cocktaildb.CocktailDBDrinkResource;
import com.ezgroceries.shoppinglist.contract.shoppinglist.CocktailResource;
import com.ezgroceries.shoppinglist.domain.CocktailEntity;
import com.ezgroceries.shoppinglist.factory.CocktailEntityFactory;
import com.ezgroceries.shoppinglist.factory.CocktailResourceFactory;
import com.ezgroceries.shoppinglist.repository.CocktailRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CocktailService {

    private final CocktailRepository cocktailRepository;
    private final CocktailResourceFactory cocktailResourceFactory;
    private final CocktailEntityFactory cocktailEntityFactory;

    public CocktailService(CocktailRepository cocktailRepository, CocktailResourceFactory cocktailResourceFactory, CocktailEntityFactory cocktailEntityFactory) {
        this.cocktailRepository = cocktailRepository;
        this.cocktailResourceFactory = cocktailResourceFactory;
        this.cocktailEntityFactory = cocktailEntityFactory;
    }

    public List<CocktailResource> persistCocktails(List<CocktailDBDrinkResource> drinks) {
        return drinks.stream()
                .map(this::createOrFetchCocktail)
                .map(cocktailResourceFactory::create)
                .collect(Collectors.toList());
    }

    public Optional<CocktailResource> findById(UUID id) {
        return cocktailRepository.findById(id).map(cocktailResourceFactory::create);
    }

    private CocktailEntity createOrFetchCocktail(CocktailDBDrinkResource drinkResource) {
        return cocktailRepository.findByDrinkId(drinkResource.getIdDrink())
                .orElseGet(() -> {
                    log.debug("Persisting new cocktail [" + drinkResource.getStrDrink() + "]");
                    return cocktailRepository.save(cocktailEntityFactory.create(drinkResource));
                });
    }
}
