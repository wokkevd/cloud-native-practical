package com.ezgroceries.shoppinglist.service;

import com.ezgroceries.shoppinglist.contract.shoppinglist.StubEmailResource;
import com.ezgroceries.shoppinglist.factory.StubEmailResourceFactory;
import com.ezgroceries.shoppinglist.repository.StubEmailRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StubEmailService {

    private final StubEmailRepository stubEmailRepository;
    private final StubEmailResourceFactory stubEmailResourceFactory;

    public StubEmailService(StubEmailRepository stubEmailRepository, StubEmailResourceFactory stubEmailResourceFactory) {
        this.stubEmailRepository = stubEmailRepository;
        this.stubEmailResourceFactory = stubEmailResourceFactory;
    }

    public List<StubEmailResource> getAllEmails() {
        return stubEmailResourceFactory.create(stubEmailRepository.findAll(Sort.by(Sort.Direction.DESC, "createdDate")));
    }
}
