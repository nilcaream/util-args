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
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class UtilArgsTest {

    @Test
    @DisplayName("Quick bind")
    void case01() {
        // given
        String[] args = of("-qv", "-n", "bob");
        TestObject target = new TestObject();

        // when
        UtilArgs.bind(args, target);

        // then
        assertThat(target.isQuick()).isTrue();
        assertThat(target.isVerbose()).isTrue();
        assertThat(target.getName()).isEqualTo("bob");
    }

    @Test
    @DisplayName("Disable last value")
    void case02() {
        // given
        String[] args = of("-n", "bob", "-n", "jack");
        TestObject target = new TestObject();

        // when
        UtilArgs.create(args, target)
                .disableUseLast()
                .bind();

        // then
        assertThat(target.getName()).isEqualTo("bob");
    }

    @Test
    @DisplayName("Fail fast on disabled first and last value")
    void case03() {
        // given
        String[] args = of("-n", "bob", "-n", "jack");
        TestObject target = new TestObject();

        // when
        assertThatExceptionOfType(UtilArgsException.class).isThrownBy(() -> {
            UtilArgs.create(args, target)
                    .disableUseFirst()
                    .disableUseLast()
                    .bind();
        }).withMessage("Cannot select single value from [bob,jack]");
    }

    @Test
    @DisplayName("Disable fail fast on disabled first and last value")
    void case04() {
        // given
        String[] args = of("-n", "bob", "-n", "jack");
        TestObject target = new TestObject();

        // when
        UtilArgs.create(args, target)
                .disableUseFirst()
                .disableUseLast()
                .disableFailFast()
                .bind();

        // then
        assertThat(target.getName()).isNull();
    }

    @Test
    @DisplayName("Do not immediately bind")
    void case05() {
        // given
        String[] args = of("-n", "bob");
        TestObject target = new TestObject();

        // when
        UtilArgs.create(args, target);

        // then
        assertThat(target.getName()).isNull();
    }

    @Test
    @DisplayName("Use custom mapper")
    void case06() {
        // given
        String[] args = of("-n", "bob");
        TestObject target = new TestObject();

        // when
        UtilArgs.create(args, target)
                .withMapper(new Mapper() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public <T> T map(String value, Class<T> cls) {
                        return (T) "not bob";
                    }
                })
                .bind();

        // then
        assertThat(target.getName()).isEqualTo("not bob");
    }

    @Test
    @DisplayName("Return operands")
    void case07() {
        // given
        String[] args = of("-n", "bob", "--", "my", "operands", "123");
        TestObject target = new TestObject();

        // when
        UtilArgs utilArgs = UtilArgs.bind(args, target);

        // then
        assertThat(utilArgs.getOperands()).isEqualTo("my operands 123");
    }

    @Test
    @DisplayName("Return blank operands")
    void case08() {
        // given
        String[] args = of("-n", "bob");
        TestObject target = new TestObject();

        // when
        UtilArgs utilArgs = UtilArgs.bind(args, target);

        // then
        assertThat(utilArgs.getOperands()).isEqualTo("");
    }

    @Test
    @DisplayName("Fail fast on setting static final field value")
    void case09() {
        // given
        String[] args = of("-f", "bob");
        TestObject target = new TestObject();

        // when
        assertThatExceptionOfType(UtilArgsException.class).isThrownBy(() -> {
            UtilArgs.create(args, target)
                    .bind();
        }).withMessage("Binding failed for FINAL_NAME");
    }

    @Test
    @DisplayName("Don't fail fast on setting static final field value")
    void case10() {
        // given
        String[] args = of("-f", "bob");
        TestObject target = new TestObject();

        // when
        UtilArgs.create(args, target)
                .disableFailFast()
                .bind();
    }

    @Test
    @DisplayName("Quick bind with single flag and String")
    void case11() {
        // given
        String[] args = of("-q", "-n", "bob");
        TestObject target = new TestObject();

        // when
        UtilArgs.bind(args, target);

        // then
        assertThat(target.isQuick()).isTrue();
        assertThat(target.isVerbose()).isFalse();
        assertThat(target.getName()).isEqualTo("bob");
    }

    @Test
    @DisplayName("Quick bind with single flag")
    void case12() {
        // given
        String[] args = of("-q");
        TestObject target = new TestObject();

        // when
        UtilArgs.bind(args, target);

        // then
        assertThat(target.isQuick()).isTrue();
        assertThat(target.isVerbose()).isFalse();
    }

    @Test
    @DisplayName("Return operands without -- marker")
    void case13() {
        // given
        String[] args = of("-n", "bob", "my", "operands", "123");
        TestObject target = new TestObject();

        // when
        UtilArgs utilArgs = UtilArgs.bind(args, target);

        // then
        assertThat(utilArgs.getOperands()).isEqualTo("my operands 123");
    }

    private String[] of(String... strings) {
        return strings;
    }
}