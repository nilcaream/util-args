package com.nilcaream.utilargs;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ArgumentProcessor2Test {

    private final ArgumentProcessor2 underTest = new ArgumentProcessor2();

    @Test
    @DisplayName("Should parse simple single-value short options")
    void simpleSingleShort() {
        // given
        String[] args = new String[]{"-a", "test a", "-b", "TEST B"};

        // when
        Map<String, List<String>> actual = underTest.process(args);

        // then
        assertThat(actual).hasSize(2);
        assertThat(actual.get("a")).containsExactly("test a");
        assertThat(actual.get("b")).containsExactly("TEST B");
    }

    @Test
    @DisplayName("Should parse simple single-value short and long options")
    void simpleSingleShortAndLong() {
        // given
        String[] args = new String[]{"--a", "test a", "-b", "TEST B"};

        // when
        Map<String, List<String>> actual = underTest.process(args);

        // then
        assertThat(actual).hasSize(2);
        assertThat(actual.get("a")).containsExactly("test a");
        assertThat(actual.get("b")).containsExactly("TEST B");
    }

    @Test
    @DisplayName("Should parse simple multi-value short and long options")
    void simpleMultipleShortAndLong() {
        // given
        String[] args = new String[]{"--a", "test a", "-b", "TEST B", "--a", "other a"};

        // when
        Map<String, List<String>> actual = underTest.process(args);

        // then
        assertThat(actual).hasSize(2);
        assertThat(actual.get("a")).containsExactly("test a", "other a");
        assertThat(actual.get("b")).containsExactly("TEST B");
    }

    @Test
    @DisplayName("Should read simple flag as last argument")
    void simpleFlagAsLast() {
        // given
        String[] args = new String[]{"--a", "test a", "-b"};

        // when
        Map<String, List<String>> actual = underTest.process(args);

        // then
        assertThat(actual).hasSize(2);
        assertThat(actual.get("a")).containsExactly("test a");
        assertThat(actual.get("b")).containsExactly("");
    }

    @Test
    @DisplayName("Should read operands")
    void operands() {
        // given
        String[] args = new String[]{"--a", "test a", "-b", "--", "operand 1", "text", "-x", "not an option"};

        // when
        Map<String, List<String>> actual = underTest.process(args);

        // then
        assertThat(actual).hasSize(3);
        assertThat(actual.get("a")).containsExactly("test a");
        // TODO consider filtering -- value
        assertThat(actual.get("b")).containsExactly("--");
        assertThat(actual.get("--")).containsExactly("operand 1 text -x not an option");
    }
}