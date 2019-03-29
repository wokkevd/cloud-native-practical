package com.ezgroceries.shoppinglist.repository;

import com.ezgroceries.shoppinglist.domain.ShoppingListEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ShoppingListRepository extends JpaRepository<ShoppingListEntity, UUID> {

    List<ShoppingListEntity> findByUserId(UUID userId);

    Optional<ShoppingListEntity> findByIdAndUserId(UUID shoppingListId, UUID userId);
}
