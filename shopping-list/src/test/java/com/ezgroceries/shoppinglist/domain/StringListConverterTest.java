package com.ezgroceries.shoppinglist.domain;

import com.ezgroceries.shoppinglist.util.StringListConverter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class StringListConverterTest {

    private static final String JOINED_STRING = "Vodka,Coffee liqueur,Light cream";
    private static final List<String> LISTED_INGREDIENTS = Arrays.asList("Vodka", "Coffee liqueur", "Light cream");

    private StringListConverter stringListConverter = new StringListConverter();

    @Test
    public void convertToDatabaseColumn() {
        String joinedString = stringListConverter.convertToDatabaseColumn(LISTED_INGREDIENTS);
        assertThat(joinedString).isEqualTo(JOINED_STRING);

    }

    @Test
    public void convertToEntityAttribute() {
        List<String> splitStringList = stringListConverter.convertToEntityAttribute(JOINED_STRING);
        assertThat(splitStringList).isNotNull();
        assertThat(splitStringList).hasSize(3);
        assertThat(splitStringList).isEqualTo(LISTED_INGREDIENTS);
    }
}