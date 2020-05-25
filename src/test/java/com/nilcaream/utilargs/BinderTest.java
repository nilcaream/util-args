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
    @DisplayName("Should set String field with default binder settings")
    void shouldSetStringWithDefaultSettings() throws NoSuchFieldException, IllegalAccessException {
        // given
        TestObject target = new TestObject();

        // when
        underTest.bind(target, get("stringField"), of("value A"));

        // then
        assertThat(target.getStringField()).isEqualTo("value A");
    }

    @Test
    @DisplayName("Should set String field with default binder settings")
    void shouldNotOverridePreviousValue() throws NoSuchFieldException, IllegalAccessException {
        // given
        TestObject target = new TestObject();
        target.setStringField("previous");

        // when
        underTest.bind(target, get("stringField"), of());

        // then
        assertThat(target.getStringField()).isEqualTo("previous");
    }

    @Test
    @DisplayName("Should use first value")
    void shouldSelectFirstValueWithDefaultSettings() throws NoSuchFieldException, IllegalAccessException {
        // given
        TestObject target = new TestObject();

        // when
        underTest.bind(target, get("stringField"), of("value A", "value X"));

        // then
        assertThat(target.getStringField()).isEqualTo("value A");
    }

    @Test
    @DisplayName("Should use last value")
    void shouldSelectLastValue() throws NoSuchFieldException, IllegalAccessException {
        // given
        underTest.withUseFirst(false);
        TestObject target = new TestObject();

        // when
        underTest.bind(target, get("stringField"), of("value A", "value X", "value Z"));

        // then
        assertThat(target.getStringField()).isEqualTo("value Z");
    }

    @Test
    @DisplayName("Should throw exception for ambiguous values")
    void shouldFailWithForAmbiguousValues() {
        // given
        underTest.withUseFirst(false).withUseLast(false);
        TestObject target = new TestObject();

        // when
        assertThatExceptionOfType(UtilArgsException.class).isThrownBy(() -> {
            underTest.bind(target, get("stringField"), of("value A", "value X", "value Z"));
        });
    }

    @Test
    @DisplayName("Should throw exception for ambiguous values")
    void shouldFailOnArrayTypesMismatch() {
        // given
        TestObject target = new TestObject();

        // when
        assertThatExceptionOfType(UtilArgsException.class).isThrownBy(() -> {
            underTest.bind(target, get("intArray"), of("10", "value X"));
        });
    }

    @Test
    @DisplayName("Should bind list of values")
    void shouldSetList() throws NoSuchFieldException, IllegalAccessException {
        // given
        TestObject target = new TestObject();

        // when
        underTest.bind(target, get("stringList"), of("value A", "value X", "value Z"));

        // then
        assertThat(target.getStringList()).containsExactly("value A", "value X", "value Z");
    }

    @Test
    @DisplayName("Should bind set of values")
    void shouldBindSet() throws NoSuchFieldException, IllegalAccessException {
        // given
        TestObject target = new TestObject();

        // when
        underTest.bind(target, get("stringSet"), of("value A", "value X", "value Z"));

        // then
        assertThat(target.getStringSet()).containsExactly("value A", "value X", "value Z");
    }

    @Test
    @DisplayName("Should bind array of String values")
    void shouldBindArrayOfStrings() throws NoSuchFieldException, IllegalAccessException {
        // given
        TestObject target = new TestObject();

        // when
        underTest.bind(target, get("stringArrayField"), of("value A", "value X", "value Z"));

        // then
        assertThat(target.getStringArrayField()).containsExactly("value A", "value X", "value Z");
    }

    @Test
    @DisplayName("Should bind array of int values")
    void shouldBindArrayOfInts() throws NoSuchFieldException, IllegalAccessException {
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