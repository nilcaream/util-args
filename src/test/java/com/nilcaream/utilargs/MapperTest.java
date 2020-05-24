package com.nilcaream.utilargs;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MapperTest {

    private final Mapper underTest = new Mapper();

    @Test
    void shouldMapPrimitiveTypes() {
        //when
        int actualInt = underTest.map(int.class, "10");
        short actualShort = underTest.map(short.class, "-20");
        long actualLong = underTest.map(long.class, "999");
        float actualFloat = underTest.map(float.class, "128");
        double actualDouble = underTest.map(double.class, "32");
        char actualChar = underTest.map(char.class, "X");
        byte actualByte = underTest.map(byte.class, "-71");
        boolean actualBoolean = underTest.map(boolean.class, "true");

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
        Integer actualInt = underTest.map(Integer.class, "10");
        Short actualShort = underTest.map(Short.class, "-20");
        Long actualLong = underTest.map(Long.class, "999");
        Float actualFloat = underTest.map(Float.class, "128");
        Double actualDouble = underTest.map(Double.class, "32");
        Character actualChar = underTest.map(Character.class, "X");
        Byte actualByte = underTest.map(Byte.class, "-71");
        Boolean actualBoolean = underTest.map(Boolean.class, "true");

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
        String actual = underTest.map(String.class, "this is text");

        // then
        assertThat(actual).isEqualTo("this is text");
    }

    @Test
    void shouldUseStringConstructor() {
        //when
        StringBuilder actual = underTest.map(StringBuilder.class, "this is text");

        // then
        assertThat(actual).isNotNull();
        assertThat(actual.toString()).isEqualTo("this is text");
    }
}