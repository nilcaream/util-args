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

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class BinderTest {

    private final Binder underTest = new Binder();

    @Test
    @DisplayName("Set String field with default binder settings")
    void case01() throws NoSuchFieldException, IllegalAccessException {
        // given
        TestObject target = new TestObject();

        // when
        underTest.bind(target, get("stringField"), of("value A"));

        // then
        assertThat(target.getStringField()).isEqualTo("value A");
    }

    @Test
    @DisplayName("Do not set value if none was provided")
    void case02() throws NoSuchFieldException, IllegalAccessException {
        // given
        TestObject target = new TestObject();
        target.setStringField("previous");

        // when
        underTest.bind(target, get("stringField"), of());

        // then
        assertThat(target.getStringField()).isEqualTo("previous");
    }

    @Test
    @DisplayName("Use first value as default")
    void case03() throws NoSuchFieldException, IllegalAccessException {
        // given
        TestObject target = new TestObject();

        // when
        underTest.bind(target, get("stringField"), of("value A", "value X"));

        // then
        assertThat(target.getStringField()).isEqualTo("value A");
    }

    @Test
    @DisplayName("Use last value if using first is disabled")
    void case04() throws NoSuchFieldException, IllegalAccessException {
        // given
        underTest.withUseFirst(false);
        TestObject target = new TestObject();

        // when
        underTest.bind(target, get("stringField"), of("value A", "value X", "value Z"));

        // then
        assertThat(target.getStringField()).isEqualTo("value Z");
    }

    @Test
    @DisplayName("Error out for ambiguous values if using both first and last is disabled")
    void case05() {
        // given
        underTest.withUseFirst(false).withUseLast(false);
        TestObject target = new TestObject();

        // when
        assertThatExceptionOfType(UtilArgsException.class).isThrownBy(() -> {
            underTest.bind(target, get("stringField"), of("value A", "value X", "value Z"));
        });
    }

    @Test
    @DisplayName("Error out for invalid array type")
    void case06() {
        // given
        TestObject target = new TestObject();

        // when
        assertThatExceptionOfType(UtilArgsException.class).isThrownBy(() -> {
            underTest.bind(target, get("intArray"), of("10", "value X"));
        });
    }

    @Test
    @DisplayName("Bind list of values")
    void case07() throws NoSuchFieldException, IllegalAccessException {
        // given
        TestObject target = new TestObject();

        // when
        underTest.bind(target, get("stringList"), of("value A", "value X", "value Z"));

        // then
        assertThat(target.getStringList()).containsExactly("value A", "value X", "value Z");
    }

    @Test
    @DisplayName("Bind set of values")
    void case08() throws NoSuchFieldException, IllegalAccessException {
        // given
        TestObject target = new TestObject();

        // when
        underTest.bind(target, get("stringSet"), of("value A", "value X", "value Z"));

        // then
        assertThat(target.getStringSet()).containsExactly("value A", "value X", "value Z");
    }

    @Test
    @DisplayName("Bind array of String values")
    void case09() throws NoSuchFieldException, IllegalAccessException {
        // given
        TestObject target = new TestObject();

        // when
        underTest.bind(target, get("stringArrayField"), of("value A", "value X", "value Z"));

        // then
        assertThat(target.getStringArrayField()).containsExactly("value A", "value X", "value Z");
    }

    @Test
    @DisplayName("Bind array of primitive types values")
    void case10() throws NoSuchFieldException, IllegalAccessException {
        // given
        TestObject target = new TestObject();

        // when
        underTest.bind(target, get("intArray"), of("10", "0", "-31"));

        // then
        assertThat(target.getIntArray()).containsExactly(10, 0, -31);
    }

    private Field get(String fieldName) throws NoSuchFieldException {
        return TestObject.class.getDeclaredField(fieldName);
    }

    private List<String> of(String... strings) {
        return Arrays.stream(strings).collect(Collectors.toList());
    }
}