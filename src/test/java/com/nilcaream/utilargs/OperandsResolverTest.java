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

class OperandsResolverTest {

    private final OperandsResolver underTest = new OperandsResolver();

    @Test
    @DisplayName("Single String short option")
    void case01() {
        // given
        Object target = new TestObject();
        String[] args = of("-n name file1 file2");

        // when
        String operands = underTest.resolve(args, target);

        // then
        assertThat(operands).isEqualTo("file1 file2");
    }

    @Test
    @DisplayName("Single String short option without operands")
    void case02() {
        // given
        Object target = new TestObject();
        String[] args = of("-n name");

        // when
        String operands = underTest.resolve(args, target);

        // then
        assertThat(operands).isEqualTo("");
    }

    @Test
    @DisplayName("String short option and flag")
    void case03() {
        // given
        Object target = new TestObject();
        String[] args = of("-n name -q file1 file2");

        // when
        String operands = underTest.resolve(args, target);

        // then
        assertThat(operands).isEqualTo("file1 file2");
    }

    @Test
    @DisplayName("Flag and String short option")
    void case04() {
        // given
        Object target = new TestObject();
        String[] args = of("-q -n name file1 file2");

        // when
        String operands = underTest.resolve(args, target);

        // then
        assertThat(operands).isEqualTo("file1 file2");
    }

    @Test
    @DisplayName("String short option and 2 flags")
    void case05() {
        // given
        Object target = new TestObject();
        String[] args = of("-n name -q -v file1 file2");

        // when
        String operands = underTest.resolve(args, target);

        // then
        assertThat(operands).isEqualTo("file1 file2");
    }

    @Test
    @DisplayName("String short option and 2 combined flags")
    void case06() {
        // given
        Object target = new TestObject();
        String[] args = of("-n name -qv file1 file2");

        // when
        String operands = underTest.resolve(args, target);

        // then
        assertThat(operands).isEqualTo("file1 file2");
    }

    @Test
    @DisplayName("String short option and flag combined with not flag")
    void case07() {
        // given
        Object target = new TestObject();
        String[] args = of("-n name -qvX file1 file2");

        // when
        String operands = underTest.resolve(args, target);

        // then
        assertThat(operands).isEqualTo("-qvX file1 file2");
    }

    @Test
    @DisplayName("String long String option and 2 combined flags")
    void case08() {
        // given
        Object target = new TestObject();
        String[] args = of("--altString name -qv file1 file2");

        // when
        String operands = underTest.resolve(args, target);

        // then
        assertThat(operands).isEqualTo("file1 file2");
    }

    @Test
    @DisplayName("String long boolean option")
    void case09() {
        // given
        Object target = new TestObject();
        String[] args = of("--verbose --altString name -q file1 file2");

        // when
        String operands = underTest.resolve(args, target);

        // then
        assertThat(operands).isEqualTo("file1 file2");
    }

    private String[] of(String string) {
        return string.split(" ");
    }
}