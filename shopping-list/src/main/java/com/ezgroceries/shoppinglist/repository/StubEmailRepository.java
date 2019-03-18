package com.ezgroceries.shoppinglist.repository;

import com.ezgroceries.shoppinglist.domain.StubEmailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StubEmailRepository extends JpaRepository<StubEmailEntity, UUID> {

}
