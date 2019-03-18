package com.ezgroceries.shoppinglist.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "SHOP_USER")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, of = {})
public class ShopUserEntity extends BaseEntity {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private boolean verified;

    @OneToMany(mappedBy = "user")
    private List<ShoppingListEntity> shoppingLists = new ArrayList<>();
}
