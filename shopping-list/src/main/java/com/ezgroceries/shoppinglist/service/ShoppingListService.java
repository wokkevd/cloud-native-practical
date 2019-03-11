package com.ezgroceries.shoppinglist.service;

import com.ezgroceries.shoppinglist.contract.shoppinglist.ShoppingListResource;
import com.ezgroceries.shoppinglist.domain.CocktailEntity;
import com.ezgroceries.shoppinglist.domain.ShoppingListEntity;
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
        Optional<ShoppingListEntity> shoppingList = shoppingListRepository.findById(shoppingListId);
        return shoppingList
                .map(shoppingListEntity -> {
                    List<CocktailEntity> foundCocktails = cocktailIds.stream()
                            .map(cocktailRepository::findById)
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .collect(Collectors.toList());
                    shoppingListEntity.getCocktails().addAll(foundCocktails);
                    shoppingListRepository.save(shoppingListEntity);
                    return foundCocktails.stream().map(CocktailEntity::getId).collect(Collectors.toList());
                })
                .orElse(null);
    }

    public ShoppingListResource getShoppingList(UUID id) {
        Optional<ShoppingListEntity> shoppingList = shoppingListRepository.findById(id);
        return shoppingList.map(shoppingListResourceFactory::create).orElse(null);
    }

    public List<ShoppingListResource> getAllShoppingLists() {
        return shoppingListResourceFactory.create(shoppingListRepository.findAll());
    }
}
