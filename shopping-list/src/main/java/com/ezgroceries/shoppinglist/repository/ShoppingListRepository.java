package com.ezgroceries.shoppinglist.repository;

import com.ezgroceries.shoppinglist.domain.ShoppingListEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ShoppingListRepository extends JpaRepository<ShoppingListEntity, UUID> {

}
