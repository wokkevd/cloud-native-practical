package com.ezgroceries.shoppinglist.service;

import com.ezgroceries.shoppinglist.contract.shoppinglist.ShoppingListResource;
import com.ezgroceries.shoppinglist.domain.CocktailEntity;
import com.ezgroceries.shoppinglist.domain.ShoppingListEntity;
import com.ezgroceries.shoppinglist.exceptions.NotFoundException;
import com.ezgroceries.shoppinglist.factory.ShoppingListResourceFactory;
import com.ezgroceries.shoppinglist.repository.CocktailRepository;
import com.ezgroceries.shoppinglist.repository.ShoppingListRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ShoppingListService {

    private static final String NOT_FOUND_MESSAGE = "Shopping list with id %s not found";

    private final ShoppingListRepository shoppingListRepository;
    private final CocktailRepository cocktailRepository;
    private final ShoppingListResourceFactory shoppingListResourceFactory;

    public ShoppingListService(ShoppingListRepository shoppingListRepository, CocktailRepository cocktailRepository, ShoppingListResourceFactory shoppingListResourceFactory) {
        this.shoppingListRepository = shoppingListRepository;
        this.cocktailRepository = cocktailRepository;
        this.shoppingListResourceFactory = shoppingListResourceFactory;
    }

    public ShoppingListResource createShoppingList(String name) {
        ShoppingListEntity shoppingListEntity = new ShoppingListEntity(name);
        return shoppingListResourceFactory.create(shoppingListRepository.save(shoppingListEntity));
    }

    public List<UUID> addCocktails(UUID shoppingListId, List<UUID> cocktailIds) {
        return shoppingListRepository.findById(shoppingListId)
                .map(shoppingListEntity -> addAvailableCocktailsToList(shoppingListEntity, cocktailIds))
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MESSAGE, shoppingListId.toString()));
    }

    public ShoppingListResource getShoppingList(UUID shoppingListId) {
        return shoppingListRepository.findById(shoppingListId)
                .map(shoppingListResourceFactory::create)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MESSAGE, shoppingListId.toString()));
    }

    public List<ShoppingListResource> getAllShoppingLists() {
        return shoppingListResourceFactory.create(shoppingListRepository.findAll());
    }

    private List<UUID> addAvailableCocktailsToList(ShoppingListEntity shoppingListEntity, List<UUID> cocktailIds) {
        List<CocktailEntity> foundCocktails = cocktailIds.stream()
                .map(cocktailRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        shoppingListEntity.getCocktails().addAll(foundCocktails);
        shoppingListRepository.save(shoppingListEntity);
        return foundCocktails.stream().map(CocktailEntity::getId).collect(Collectors.toList());
    }
}
