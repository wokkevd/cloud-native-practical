package com.ezgroceries.shoppinglist.controller;

import com.ezgroceries.shoppinglist.contract.shoppinglist.ShopUserResource;
import com.ezgroceries.shoppinglist.service.ShopUserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping(produces = "application/json")
@Slf4j
public class ShopUserController {

    private final ShopUserService shopUserService;
    private final String tokenHeaderName;

    public ShopUserController(ShopUserService shopUserService, @Value("${security.token}") String tokenHeaderName) {
        this.shopUserService = shopUserService;
        this.tokenHeaderName = tokenHeaderName;
    }

    @PostMapping("/registrations")
    public ResponseEntity<ShopUserResource> registerUser(@Valid @RequestBody UserRegistrationResource requestResource) {
        log.info("Received request at " + LocalDateTime.now());
        return ResponseEntity.ok(shopUserService.registerUser(requestResource.getFirstName(), requestResource.getLastName(),
                requestResource.getEmail(), requestResource.getPassword()));
    }

    @PostMapping("/verifications")
    public ResponseEntity<ShopUserResource> verifyUser(@Valid @RequestBody VerificationResource verificationResource) {
        ShopUserResource shopUserResource = shopUserService.verifyUser(verificationResource.getUserId(), verificationResource.getVerificationCode());
        return ResponseEntity.ok(shopUserResource);
    }

    @PostMapping("/logins")
    public ResponseEntity loginUser(@Valid @RequestBody LoginResource loginResource) {
        String token = shopUserService.loginUser(loginResource.getEmail(), loginResource.getPassword());
        return ResponseEntity.noContent().header(tokenHeaderName, token).build();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class UserRegistrationResource {

        @NotNull
        private String email;
        @NotNull
        private String firstName;
        @NotNull
        private String lastName;
        @NotNull
        private String password;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class LoginResource {

        @NotNull
        private String email;
        @NotNull
        private String password;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class VerificationResource {

        @NotNull
        private String verificationCode;
        @NotNull
        private UUID userId;
    }
}
