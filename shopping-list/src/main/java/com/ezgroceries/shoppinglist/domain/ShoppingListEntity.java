package com.ezgroceries.shoppinglist.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "SHOPPING_LIST")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, of = {})
public class ShoppingListEntity extends BaseEntity {

    private String name;

    @ManyToMany
    @JoinTable(name = "cocktail_shopping_list", joinColumns = @JoinColumn(name = "shopping_list_id", referencedColumnName = "id"), inverseJoinColumns =
    @JoinColumn(name = "cocktail_id", referencedColumnName = "id"))
    private Set<CocktailEntity> cocktails = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private ShopUserEntity owner;

    public ShoppingListEntity(String name) {
        this.name = name;
    }
}
