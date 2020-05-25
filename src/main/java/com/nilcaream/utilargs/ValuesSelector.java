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

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Optional.ofNullable;

/**
 * Mapped arguments value selector.
 */
public class ValuesSelector {

    /**
     * Selects matching values from provided mapped arguments based on Option
     * value and alternative.
     *
     * @param arguments mapped arguments.
     * @param option    Option from target object's field.
     * @return list of values in value, alternative order without duplicates.
     */
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
