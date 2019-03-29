package com.ezgroceries.shoppinglist.controller;

import com.ezgroceries.shoppinglist.contract.shoppinglist.StubEmailResource;
import com.ezgroceries.shoppinglist.service.StubEmailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/email-stubs", produces = "application/json")
public class StubEmailController {

    private final StubEmailService stubEmailService;

    public StubEmailController(StubEmailService stubEmailService) {
        this.stubEmailService = stubEmailService;
    }

    @GetMapping
    public List<StubEmailResource> getAllEmails() {
        return stubEmailService.getAllEmails();
    }

}
