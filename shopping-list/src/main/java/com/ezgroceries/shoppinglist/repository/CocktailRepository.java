package com.ezgroceries.shoppinglist.repository;

import com.ezgroceries.shoppinglist.domain.CocktailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CocktailRepository extends JpaRepository<CocktailEntity, UUID> {

    Optional<CocktailEntity> findByDrinkId(String drinkId);
}
