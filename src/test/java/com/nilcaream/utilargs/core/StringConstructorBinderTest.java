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

package com.nilcaream.utilargs.core;

import com.nilcaream.utilargs.model.Parameter;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.fest.assertions.Assertions.assertThat;

/**
 * TODO
 * <p/>
 * Krzysztof Smigielski 10/30/12 8:12 PM
 */
public class StringConstructorBinderTest extends AbstractBinderTest {

    @Before
    public void setUp() {
        binder = new StringConstructorBinder();
    }

    @Test
    public void bindStringBuilder() throws Exception {
        doAssertAsString(new TestObject(), "stringBuilder", "test 10", new StringBuilder("test 10"));
    }

    @Test
    public void bindStringBuffer() throws Exception {
        doAssertAsString(new TestObject(), "stringBuffer", "test 10", new StringBuffer("test 10"));
    }

    @Test
    public void bindBigDecimal() throws Exception {
        doAssert(new TestObject(), "bigDecimal", "1.23456789012345", new BigDecimal("1.23456789012345"));
    }

    @Test
    public void bindBigInteger() throws Exception {
        doAssert(new TestObject(), "bigInteger", "123456789012345", new BigInteger("123456789012345"));
    }

    protected void doAssertAsString(Object wrapper, String fieldName, String actual, Object expected) throws Exception {
        // given
        Parameter parameter = createFrom(wrapper, fieldName, actual);

        // when
        binder.bind(parameter, wrapper);

        // then
        assertThat(wrapper.getClass().getDeclaredField(fieldName).get(wrapper).toString()).isEqualTo(expected.toString());
    }

    private static class TestObject {
        StringBuffer stringBuffer;
        StringBuilder stringBuilder;
        BigDecimal bigDecimal;
        BigInteger bigInteger;
    }
}
