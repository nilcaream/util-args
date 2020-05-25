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

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ArgumentsParserTest {

    private final ArgumentsParser underTest = new ArgumentsParser();

    @Test
    @DisplayName("Simple single-value short options")
    void case01() {
        // given
        String[] args = new String[]{"-a", "test a", "-b", "TEST B"};

        // when
        Map<String, List<String>> actual = underTest.parse(args);

        // then
        assertThat(actual).hasSize(2);
        assertThat(actual.get("a")).containsExactly("test a");
        assertThat(actual.get("b")).containsExactly("TEST B");
    }

    @Test
    @DisplayName("Simple single-value short and long options")
    void case02() {
        // given
        String[] args = new String[]{"--a", "test a", "-b", "TEST B"};

        // when
        Map<String, List<String>> actual = underTest.parse(args);

        // then
        assertThat(actual).hasSize(2);
        assertThat(actual.get("a")).containsExactly("test a");
        assertThat(actual.get("b")).containsExactly("TEST B");
    }

    @Test
    @DisplayName("Simple multi-value short and long options")
    void case03() {
        // given
        String[] args = new String[]{"--a", "test a", "-b", "TEST B", "--a", "other a"};

        // when
        Map<String, List<String>> actual = underTest.parse(args);

        // then
        assertThat(actual).hasSize(2);
        assertThat(actual.get("a")).containsExactly("test a", "other a");
        assertThat(actual.get("b")).containsExactly("TEST B");
    }

    @Test
    @DisplayName("Simple flag as last argument")
    void case04() {
        // given
        String[] args = new String[]{"--a", "test a", "-b"};

        // when
        Map<String, List<String>> actual = underTest.parse(args);

        // then
        assertThat(actual).hasSize(2);
        assertThat(actual.get("a")).containsExactly("test a");
        assertThat(actual.get("b")).containsExactly("true");
    }

    @Test
    @DisplayName("Operands support")
    void case05() {
        // given
        String[] args = new String[]{"--a", "test a", "-b", "--", "operand 1", "text", "-x", "not an option"};

        // when
        Map<String, List<String>> actual = underTest.parse(args);

        // then
        assertThat(actual).hasSize(3);
        assertThat(actual.get("a")).containsExactly("test a");
        assertThat(actual.get("b")).containsExactly("true");
        assertThat(actual.get("--")).containsExactly("operand 1 text -x not an option");
    }

    @Test
    @DisplayName("Ignore missing operands")
    void case06() {
        // given
        String[] args = new String[]{"--a", "test a", "-b", "--"};

        // when
        Map<String, List<String>> actual = underTest.parse(args);

        // then
        assertThat(actual).hasSize(2);
        assertThat(actual.get("a")).containsExactly("test a");
        assertThat(actual.get("b")).containsExactly("true");
    }

    @Test
    @DisplayName("Skip duplicated values")
    void case07() {
        // given
        String[] args = new String[]{"--a", "test a", "-b", "b value", "-a", "test a"};

        // when
        Map<String, List<String>> actual = underTest.parse(args);

        // then
        assertThat(actual).hasSize(2);
        assertThat(actual.get("a")).containsExactly("test a");
        assertThat(actual.get("b")).containsExactly("b value");
    }

    @Test
    @DisplayName("Set value even if it can be interpreted as an option")
    void case08() {
        // given
        String[] args = new String[]{"--a", "test a", "-b", "-a"};

        // when
        Map<String, List<String>> actual = underTest.parse(args);

        // then
        assertThat(actual).hasSize(2);
        assertThat(actual.get("a")).containsExactly("test a", "true");
        assertThat(actual.get("b")).containsExactly("-a");
    }

    @Test
    @DisplayName("Unpack flag")
    void case09() {
        // given
        String[] args = new String[]{"-abc"};

        // when
        Map<String, List<String>> actual = underTest.parse(args);

        // then
        assertThat(actual).hasSize(3);
        assertThat(actual.get("a")).containsExactly("true");
        assertThat(actual.get("b")).containsExactly("true");
        assertThat(actual.get("c")).containsExactly("true");
    }
}