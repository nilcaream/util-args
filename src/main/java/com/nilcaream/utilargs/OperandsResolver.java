/*
 * Copyright 2020 Krzysztof Smigielski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nilcaream.utilargs;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class OperandsResolver {

    public String resolve(String[] args, Object... targets) {
        Set<Option> booleanOptions = findOptions(targets, field -> isBoolean(field.getType()));
        Set<String> booleanValues = findValues(booleanOptions);
        Set<String> booleanAlternatives = findAlternatives(booleanOptions);

        Set<Option> allOptions = findOptions(targets, field -> true);
        Set<String> allValues = findValues(allOptions);
        Set<String> allAlternatives = findAlternatives(allOptions);

        int operandsIndex = 0;
        Pattern combinedBooleanValuesPattern = booleanValues.isEmpty() ? null : Pattern.compile("[" + String.join("", booleanValues) + "]+");

        for (int index = 0, length = args.length; index < length; index++) {
            String arg = args[index];
            if (arg.startsWith("--") && arg.length() > 2) {
                String key = arg.substring(2);
                if (booleanAlternatives.contains(key)) { // --verbose
                    operandsIndex = index + 1;
                } else if (allAlternatives.contains(key)) { // --file input.txt
                    operandsIndex = index + 2;
                }
            } else if (arg.startsWith("-") && arg.length() == 2) {
                String key = arg.substring(1);
                if (booleanValues.contains(key)) { // -v
                    operandsIndex = index + 1;
                } else if (allValues.contains(key)) { // -f input.txt
                    operandsIndex = index + 2;
                }
            } else if (arg.startsWith("-") && arg.length() > 2) {
                String key = arg.substring(1);
                if (combinedBooleanValuesPattern != null && combinedBooleanValuesPattern.matcher(key).matches()) { // -cjvf
                    operandsIndex = index + 1;
                }
            }
        }

        return String.join(" ", Arrays.copyOfRange(args, operandsIndex, args.length));
    }

    private Set<String> findValues(Set<Option> options) {
        return options.stream()
                .map(Option::value)
                .filter(value -> !value.isEmpty())
                .map(value -> value.substring(0, 1))
                .collect(Collectors.toSet());
    }

    private Set<String> findAlternatives(Set<Option> options) {
        return options.stream()
                .map(Option::alternative)
                .filter(alternative -> !alternative.isEmpty())
                .collect(Collectors.toSet());
    }

    private Set<Option> findOptions(Object[] targets, Predicate<Field> filter) {
        return Arrays.stream(targets)
                .map(target -> target.getClass().getDeclaredFields())
                .map(fields -> Arrays.stream(fields).collect(Collectors.toSet()))
                .flatMap(Collection::stream)
                .filter(filter)
                .map(field -> field.getAnnotation(Option.class))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private boolean isBoolean(Class<?> cls) {
        return cls.equals(boolean.class) || cls.equals(Boolean.class);
    }
}
