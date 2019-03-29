package com.ezgroceries.shoppinglist.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "COCKTAIL")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, of = {})
public class CocktailEntity extends BaseEntity {

    @Column(name = "ID_DRINK")
    private String drinkId;
    private String name;
    private String glass;
    private String url;
    private String instructions;
    @Convert(converter = StringListConverter.class)
    @Builder.Default
    private List<String> ingredients = new ArrayList<>();
}
