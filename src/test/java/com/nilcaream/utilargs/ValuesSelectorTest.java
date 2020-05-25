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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class ValuesSelectorTest {

    private final ValuesSelector underTest = new ValuesSelector();

    @Test
    @DisplayName("Select single element by option value")
    void case01() throws NoSuchFieldException {
        // given
        Map<String, List<String>> arguments = new HashMap<>();
        arguments.put("string", of("single"));
        arguments.put("stringValue", of("other"));

        // when
        List<String> actual = underTest.select(arguments, get("stringField"));

        // then
        assertThat(actual).containsExactly("single");
    }

    @Test
    @DisplayName("Select single element by option alternative")
    void case02() throws NoSuchFieldException {
        // given
        Map<String, List<String>> arguments = new HashMap<>();
        arguments.put("altString", of("single alt"));
        arguments.put("stringValue", of("other"));

        // when
        List<String> actual = underTest.select(arguments, get("stringField"));

        // then
        assertThat(actual).containsExactly("single alt");
    }

    @Test
    @DisplayName("Select multiple elements")
    void case03() throws NoSuchFieldException {
        // given
        Map<String, List<String>> arguments = new HashMap<>();
        arguments.put("string", of("first", "second"));
        arguments.put("stringArray", of("sa1", "sa2"));

        // when
        List<String> actual = underTest.select(arguments, get("stringField"));

        // then
        assertThat(actual).containsExactly("first", "second");
    }

    @Test
    @DisplayName("Do not change order for value and alternative")
    void case04() throws NoSuchFieldException {
        // given
        Map<String, List<String>> arguments = new HashMap<>();
        arguments.put("string", of("first", "second"));
        arguments.put("altString", of("third", "fourth", "other"));
        arguments.put("stringArray", of("sa1", "sa2"));

        // when
        List<String> actual = underTest.select(arguments, get("stringField"));

        // then
        assertThat(actual).containsExactly("first", "second", "third", "fourth", "other");
    }

    @Test
    @DisplayName("Do not add duplicates for value and alternative")
    void case05() throws NoSuchFieldException {
        // given
        Map<String, List<String>> arguments = new HashMap<>();
        arguments.put("string", of("first", "second"));
        arguments.put("stringArray", of("sa1", "sa2"));
        arguments.put("altString", of("second", "third", "fourth", "other", "first"));

        // when
        List<String> actual = underTest.select(arguments, get("stringField"));

        // then
        assertThat(actual).containsExactly("first", "second", "third", "fourth", "other");
    }

    private Option get(String fieldName) throws NoSuchFieldException {
        return TestObject.class.getDeclaredField(fieldName).getAnnotation(Option.class);
    }

    private List<String> of(String... strings) {
        return Arrays.stream(strings).collect(Collectors.toList());
    }
}