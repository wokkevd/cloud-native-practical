package com.ezgroceries.shoppinglist.security;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ShopUserPrincipal {

    private UUID userId;

    public boolean isAuthorisedToRequest(UUID requestUserId) {
        return Objects.equals(userId, requestUserId);
    }
}
