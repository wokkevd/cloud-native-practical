package com.ezgroceries.shoppinglist.factory;

import com.ezgroceries.shoppinglist.contract.shoppinglist.StubEmailResource;
import com.ezgroceries.shoppinglist.domain.StubEmailEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StubEmailResourceFactory {

    private StubEmailResource create(StubEmailEntity stubEmailEntity) {
        return StubEmailResource.builder()
                .toAddress(stubEmailEntity.getToAddress())
                .subject(stubEmailEntity.getSubject())
                .emailText(stubEmailEntity.getEmailText())
                .createdDate(stubEmailEntity.getCreatedDate())
                .build();
    }

    public List<StubEmailResource> create(List<StubEmailEntity> stubEmailEntities) {
        return stubEmailEntities.stream().map(this::create).collect(Collectors.toList());
    }
}
