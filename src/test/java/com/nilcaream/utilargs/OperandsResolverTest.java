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