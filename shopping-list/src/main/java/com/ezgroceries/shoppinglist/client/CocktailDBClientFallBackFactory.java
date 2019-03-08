package com.ezgroceries.shoppinglist.client;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class CocktailDBClientFallBackFactory implements FallbackFactory<CocktailDBClient> {

    @Override
    public CocktailDBClient create(Throwable throwable) {
        return new CocktailDBClientFallBack(throwable);
    }
}
