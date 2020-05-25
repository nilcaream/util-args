package com.nilcaream.utilargs;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MapperTest {

    private final Mapper underTest = new Mapper();

    @Test
    void shouldMapPrimitiveTypes() {
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
    void shouldMapWrapperTypes() {
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
    void shouldMapString() {
        //when
        String actual = underTest.map("this is text", String.class);

        // then
        assertThat(actual).isEqualTo("this is text");
    }

    @Test
    void shouldUseStringConstructor() {
        //when
        StringBuilder actual = underTest.map("this is text", StringBuilder.class);

        // then
        assertThat(actual).isNotNull();
        assertThat(actual.toString()).isEqualTo("this is text");
    }
}