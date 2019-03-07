package com.ezgroceries.shoppinglist.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "cocktailId")
public class CocktailResource {

    private UUID cocktailId;
    private String name;
    private String glass;
    private String instructions;
    private String image;
    @Builder.Default
    private List<String> ingredients = new ArrayList<>();
}
