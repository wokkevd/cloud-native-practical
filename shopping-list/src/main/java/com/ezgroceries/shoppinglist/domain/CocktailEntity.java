package com.ezgroceries.shoppinglist.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "COCKTAIL")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CocktailEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id")
    private UUID id;
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
