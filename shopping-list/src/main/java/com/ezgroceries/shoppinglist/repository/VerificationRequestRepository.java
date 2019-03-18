package com.ezgroceries.shoppinglist.repository;

import com.ezgroceries.shoppinglist.domain.VerificationRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface VerificationRequestRepository extends JpaRepository<VerificationRequestEntity, UUID> {

    Optional<VerificationRequestEntity> findTopByUserIdOrderByCreatedDateDesc(UUID userId);
}
