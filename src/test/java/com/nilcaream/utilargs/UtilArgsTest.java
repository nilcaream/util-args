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

    private String[] of(String... strings) {
        return strings;
    }
}