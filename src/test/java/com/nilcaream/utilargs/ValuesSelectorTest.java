package com.nilcaream.utilargs;

import com.nilcaream.utilargs.model.Option;
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
    @DisplayName("Should select single element by value")
    void shouldSelectSingle() throws NoSuchFieldException {
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
    @DisplayName("Should select single element by alternative value")
    void shouldSelectSingleByAlternative() throws NoSuchFieldException {
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
    @DisplayName("Should select multiple elements")
    void shouldSelectMultiple() throws NoSuchFieldException {
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
    @DisplayName("Should not change order for value and alternative")
    void shouldNotChangeOrder() throws NoSuchFieldException {
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
    @DisplayName("Should not add duplicats for value and alternative")
    void shouldNotAddDuplicates() throws NoSuchFieldException {
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