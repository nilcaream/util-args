package com.nilcaream.utilargs;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Optional.ofNullable;

public class ValuesSelector {

    public List<String> select(Map<String, List<String>> arguments, Option option) {
        return combineValues(arguments.get(option.value()), arguments.get(option.alternative()));
    }

    private List<String> combineValues(List<String> valuesA, List<String> valuesB) {
        if (valuesA == null && valuesB == null) {
            return null;
        } else {
            Set<String> values = new LinkedHashSet<>();
            ofNullable(valuesA).ifPresent(values::addAll);
            ofNullable(valuesB).ifPresent(values::addAll);
            return new ArrayList<>(values);
        }
    }
}
