/*
 * Copyright 2013 Krzysztof Smigielski
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

import com.nilcaream.utilargs.model.Option;
import org.junit.Test;

import java.math.BigDecimal;

import static org.fest.assertions.Assertions.assertThat;

/**
 * TODO
 * <p/>
 * Krzysztof Smigielski 10/30/12 10:38 PM
 */
public class UtilArgsTest {

    @Test
    public void nothingBound() {
        // given
        String operands = "-a 1 -b 2 -t";
        String[] args = operands.split(" ");
        Object wrapper = new Object();

        // when
        UtilArgs ua = new UtilArgs(args, wrapper);

        // then
        assertThat(args).isEqualTo(ua.getArguments());
        assertThat(wrapper).isEqualTo(ua.getWrapper());
        assertThat(ua.getOperands()).isEqualTo(operands);
    }

    @Test
    public void notExistingParameters() {
        // given
        String[] args = "-a 1 -v 2 -t 6".split(" ");
        TestObject wrapper = new TestObject();

        // when
        UtilArgs ua = new UtilArgs(args, wrapper);

        // then
        assertThat(wrapper.amount).isEqualTo(BigDecimal.ONE);
        assertThat(wrapper.value).isEqualTo(2f);
        assertThat(ua.getOperands()).isEqualTo("-t 6");
    }

    @Test
    public void allInvalidArguments() {
        // given
        String operands = "1 - s ks 0- --  . ";
        String[] args = operands.split(" ");
        TestObject wrapper = new TestObject();

        // when
        UtilArgs ua = new UtilArgs(args, wrapper);

        // then
        assertThat(wrapper).isEqualTo(new TestObject());
        assertThat(ua.getOperands()).isEqualTo(operands.trim());
    }

    @Test
    public void someInvalidArguments() {
        // given
        String operands = "-n ks 0- --  . ";
        String[] args = operands.split(" ");
        TestObject wrapper = new TestObject();
        TestObject expected = new TestObject();
        expected.name = "ks";

        // when
        UtilArgs ua = new UtilArgs(args, wrapper);

        // then
        assertThat(wrapper).isEqualTo(expected);
        assertThat(ua.getOperands()).isEqualTo("0- --  .");
    }

    @Test
    public void operandsDelimiter() {
        // given
        String operands = "-n ks --  . ";
        String[] args = operands.split(" ");
        TestObject wrapper = new TestObject();
        TestObject expected = new TestObject();
        expected.name = "ks";

        // when
        UtilArgs ua = new UtilArgs(args, wrapper);

        // then
        assertThat(wrapper).isEqualTo(expected);
        assertThat(ua.getOperands()).isEqualTo(".");
    }

    @Test
    public strictfp void setAll() {
        // given
        String[] args = "-v 2.345e2 -f true -n test -a 921.99911001 other args 1".split(" ");
        TestObject wrapper = new TestObject();

        // when
        UtilArgs ua = new UtilArgs(args, wrapper);

        // then
        assertThat(wrapper.flag).isTrue();
        assertThat(wrapper.value).isEqualTo(234.5f);
        assertThat(wrapper.name).isEqualTo("test");
        assertThat(wrapper.amount).isEqualTo(new BigDecimal("921.99911001"));
        assertThat(ua.getOperands()).isEqualTo("other args 1");
    }

    @Test
    public strictfp void ignoreFalseBoolean() {
        // given
        String[] args = "-v 2.345e2 -f false -n test -a 921.99911001 other args 1".split(" ");
        TestObject wrapper = new TestObject();

        // when
        UtilArgs ua = new UtilArgs(args, wrapper);

        // then
        assertThat(wrapper.flag).isTrue();
        assertThat(wrapper.more).isFalse();
        assertThat(wrapper.value).isEqualTo(234.5f);
        assertThat(wrapper.name).isEqualTo("test");
        assertThat(wrapper.amount).isEqualTo(new BigDecimal("921.99911001"));
        assertThat(ua.getOperands()).isEqualTo("other args 1");
    }

    @Test
    public strictfp void setBooleanGroup() {
        // given
        String[] args = "-v 2.345e2 -fm -n test -a 921.99911001 other args 1".split(" ");
        TestObject wrapper = new TestObject();

        // when
        UtilArgs ua = new UtilArgs(args, wrapper);

        // then
        assertThat(wrapper.flag).isTrue();
        assertThat(wrapper.more).isTrue();
        assertThat(wrapper.value).isEqualTo(234.5f);
        assertThat(wrapper.name).isEqualTo("test");
        assertThat(wrapper.amount).isEqualTo(new BigDecimal("921.99911001"));
        assertThat(ua.getOperands()).isEqualTo("other args 1");
    }

    @Test
    public strictfp void ignoreFalseInBooleanGroup() {
        // given
        String[] args = "-v 2.345e2 -fm false -n test -a 921.99911001 other args 1".split(" ");
        TestObject wrapper = new TestObject();

        // when
        UtilArgs ua = new UtilArgs(args, wrapper);

        // then
        assertThat(wrapper.flag).isTrue();
        assertThat(wrapper.more).isTrue();
        assertThat(wrapper.value).isEqualTo(234.5f);
        assertThat(wrapper.name).isEqualTo("test");
        assertThat(wrapper.amount).isEqualTo(new BigDecimal("921.99911001"));
        assertThat(ua.getOperands()).isEqualTo("other args 1");
    }

    @Test
    public strictfp void setBlankBoolean() {
        // given
        String[] args = "-v 2.345e2 -f -n test -a 921.99911001 other args 1".split(" ");
        TestObject wrapper = new TestObject();

        // when
        UtilArgs ua = new UtilArgs(args, wrapper);

        // then
        assertThat(wrapper.flag).isTrue();
        assertThat(wrapper.value).isEqualTo(234.5f);
        assertThat(wrapper.name).isEqualTo("test");
        assertThat(wrapper.amount).isEqualTo(new BigDecimal("921.99911001"));
        assertThat(ua.getOperands()).isEqualTo("other args 1");
    }

    private static final class TestObject {

        @Option(name = 'f')
        private boolean flag;

        @Option(name = 'm')
        private boolean more;

        @Option(name = 'v')
        private float value;

        @Option(name = 'n')
        private String name;

        @Option(name = 'a')
        private BigDecimal amount;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TestObject that = (TestObject) o;

            if (flag != that.flag) return false;
            if (more != that.more) return false;
            if (Float.compare(that.value, value) != 0) return false;
            if (amount != null ? !amount.equals(that.amount) : that.amount != null) return false;
            if (name != null ? !name.equals(that.name) : that.name != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = (flag ? 1 : 0);
            result = 31 * result + (more ? 1 : 0);
            result = 31 * result + (value != +0.0f ? Float.floatToIntBits(value) : 0);
            result = 31 * result + (name != null ? name.hashCode() : 0);
            result = 31 * result + (amount != null ? amount.hashCode() : 0);
            return result;
        }
    }
}
