package com.ezgroceries.shoppinglist.domain;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {

    @Override
    public String convertToDatabaseColumn(List<String> list) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.stream()
                .filter(s -> !StringUtils.isEmpty(s))
                .distinct()
                .collect(Collectors.joining(","));
    }

    @Override
    public List<String> convertToEntityAttribute(String joined) {
        if (StringUtils.isEmpty(joined)) {
            return new ArrayList<>();
        }
        return Lists.newArrayList(Splitter.on(",").split(joined));
    }
}