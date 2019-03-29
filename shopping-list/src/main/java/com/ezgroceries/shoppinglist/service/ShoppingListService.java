package com.ezgroceries.shoppinglist.service;

import com.ezgroceries.shoppinglist.contract.shoppinglist.ShoppingListResource;
import com.ezgroceries.shoppinglist.domain.CocktailEntity;
import com.ezgroceries.shoppinglist.domain.ShoppingListEntity;
import com.ezgroceries.shoppinglist.exception.BadRequestException;
import com.ezgroceries.shoppinglist.exception.NotFoundException;
import com.ezgroceries.shoppinglist.factory.ShoppingListResourceFactory;
import com.ezgroceries.shoppinglist.repository.CocktailRepository;
import com.ezgroceries.shoppinglist.repository.ShopUserRepository;
import com.ezgroceries.shoppinglist.repository.ShoppingListRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ShoppingListService {

    private static final String NOT_FOUND_MESSAGE = "Shopping list with id %s for user %s not found";
    private static final String BAD_REQUEST_MESSAGE = "Unable to create shopping list.";

    private final ShoppingListRepository shoppingListRepository;
    private final CocktailRepository cocktailRepository;
    private final ShopUserRepository shopUserRepository;
    private final ShoppingListResourceFactory shoppingListResourceFactory;

    public ShoppingListService(ShoppingListRepository shoppingListRepository, CocktailRepository cocktailRepository, ShopUserRepository shopUserRepository, ShoppingListResourceFactory shoppingListResourceFactory) {
        this.shoppingListRepository = shoppingListRepository;
        this.cocktailRepository = cocktailRepository;
        this.shopUserRepository = shopUserRepository;
        this.shoppingListResourceFactory = shoppingListResourceFactory;
    }

    public ShoppingListResource createShoppingList(String name, UUID userId) {
        ShoppingListEntity shoppingListEntity = new ShoppingListEntity(name);
        shoppingListEntity.setUser(shopUserRepository.findById(userId).orElseThrow(() -> new BadRequestException(BAD_REQUEST_MESSAGE)));
        return shoppingListResourceFactory.create(shoppingListRepository.save(shoppingListEntity));
    }

    public List<UUID> addCocktails(UUID shoppingListId, UUID userId, List<UUID> cocktailIds) {
        ShoppingListEntity shoppingList = getShoppingListEntity(shoppingListId, userId);
        return addAvailableCocktailsToList(shoppingList, cocktailIds);
    }

    public ShoppingListResource getShoppingList(UUID shoppingListId, UUID userId) {
        return shoppingListResourceFactory.create(getShoppingListEntity(shoppingListId, userId));
    }

    public List<ShoppingListResource> getAllShoppingLists(UUID userId) {
        return shoppingListResourceFactory.create(shoppingListRepository.findByUserId(userId));
    }

    private ShoppingListEntity getShoppingListEntity(UUID shoppingListId, UUID userId) {
        return shoppingListRepository.findByIdAndUserId(shoppingListId, userId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MESSAGE, shoppingListId.toString(), userId.toString()));
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
