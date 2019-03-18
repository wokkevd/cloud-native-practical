package com.ezgroceries.shoppinglist.contract.shoppinglist;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShopUserResource {

    private UUID userId;
    private String firstName;
    private String lastName;
    private String email;
    private boolean verified;
}
