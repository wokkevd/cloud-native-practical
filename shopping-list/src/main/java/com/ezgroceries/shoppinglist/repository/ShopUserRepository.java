package com.ezgroceries.shoppinglist.repository;

import com.ezgroceries.shoppinglist.domain.ShopUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ShopUserRepository extends JpaRepository<ShopUserEntity, UUID> {

    Optional<ShopUserEntity> findByEmail(String email);
}
