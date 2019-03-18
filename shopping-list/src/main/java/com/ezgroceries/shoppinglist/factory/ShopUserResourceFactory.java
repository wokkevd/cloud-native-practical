package com.ezgroceries.shoppinglist.factory;

import com.ezgroceries.shoppinglist.contract.shoppinglist.ShopUserResource;
import com.ezgroceries.shoppinglist.domain.ShopUserEntity;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

@Component
public class ShopUserResourceFactory {

    public ShopUserResource create(ShopUserEntity shopUserEntity) {
        return ShopUserResource.builder()
                .userId(shopUserEntity.getId())
                .firstName(shopUserEntity.getFirstName())
                .lastName(shopUserEntity.getLastName())
                .email(shopUserEntity.getEmail())
                .verified(shopUserEntity.isVerified())
                .build();
    }
}
