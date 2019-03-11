package com.ezgroceries.shoppinglist.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "SHOPPING_LIST")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingListEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id")
    private UUID id;
    private String name;

    @ManyToMany
    @JoinTable(name = "cocktail_shopping_list", joinColumns = @JoinColumn(name = "shopping_list_id", referencedColumnName = "id"), inverseJoinColumns =
    @JoinColumn(name = "cocktail_id", referencedColumnName = "id"))
    private Set<CocktailEntity> cocktails = new HashSet<>();

    public ShoppingListEntity(String name) {
        this.name = name;
    }
}
