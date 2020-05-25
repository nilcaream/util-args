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

import static org.assertj.core.api.Assertions.assertThat;

class MapperTest {

    private final Mapper underTest = new BaseMapper();

    @Test
    @DisplayName("Map primitive types")
    void case01() {
        //when
        int actualInt = underTest.map("10", int.class);
        short actualShort = underTest.map("-20", short.class);
        long actualLong = underTest.map("999", long.class);
        float actualFloat = underTest.map("128", float.class);
        double actualDouble = underTest.map("32", double.class);
        char actualChar = underTest.map("X", char.class);
        byte actualByte = underTest.map("-71", byte.class);
        boolean actualBoolean = underTest.map("true", boolean.class);

        // then
        assertThat(actualInt).isEqualTo(10);
        assertThat(actualShort).isEqualTo((short) -20);
        assertThat(actualLong).isEqualTo(999L);
        assertThat(actualFloat).isEqualTo(128f);
        assertThat(actualDouble).isEqualTo(32d);
        assertThat(actualChar).isEqualTo('X');
        assertThat(actualByte).isEqualTo((byte) -71);
        assertThat(actualBoolean).isEqualTo(true);
    }

    @Test
    @DisplayName("Map boxing (wrapper) types")
    void case02() {
        //when
        Integer actualInt = underTest.map("10", Integer.class);
        Short actualShort = underTest.map("-20", Short.class);
        Long actualLong = underTest.map("999", Long.class);
        Float actualFloat = underTest.map("128", Float.class);
        Double actualDouble = underTest.map("32", Double.class);
        Character actualChar = underTest.map("X", Character.class);
        Byte actualByte = underTest.map("-71", Byte.class);
        Boolean actualBoolean = underTest.map("true", Boolean.class);

        // then
        assertThat(actualInt).isEqualTo(10);
        assertThat(actualShort).isEqualTo((short) -20);
        assertThat(actualLong).isEqualTo(999L);
        assertThat(actualFloat).isEqualTo(128f);
        assertThat(actualDouble).isEqualTo(32d);
        assertThat(actualChar).isEqualTo('X');
        assertThat(actualByte).isEqualTo((byte) -71);
        assertThat(actualBoolean).isEqualTo(true);
    }

    @Test
    @DisplayName("Trivial String mapping")
    void case03() {
        //when
        String actual = underTest.map("this is text", String.class);

        // then
        assertThat(actual).isEqualTo("this is text");
    }

    @Test
    @DisplayName("String constructor mapping")
    void case04() {
        //when
        StringBuilder actual = underTest.map("this is text", StringBuilder.class);

        // then
        assertThat(actual).isNotNull();
        assertThat(actual.toString()).isEqualTo("this is text");
    }

    @Test
    @DisplayName("Do not fail for non matching strategy found")
    void case05() {
        //when
        Object actual = underTest.map("this is text", Object.class);

        // then
        assertThat(actual).isNull();
    }
}