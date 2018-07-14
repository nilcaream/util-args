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

import org.junit.Test;

/**
 * TODO
 * <p/>
 * Krzysztof Smigielski 1/27/13 1:18 PM
 */
public abstract class AbstractStaticValueOfBinderTest extends AbstractBinderTest {

    protected abstract Object getWrapper();

    @Test
    public void bindBooleanTrue() throws Exception {
        doAssert(getWrapper(), "booleanValue", "true", true);
    }

    @Test
    public void bindBooleanFalse() throws Exception {
        doAssert(getWrapper(), "booleanValue", "false", false);
    }

    @Test
    public void bindBooleanBlankAsFalse() throws Exception {
        doAssert(getWrapper(), "booleanValue", "", false);
    }

    @Test
    public void bindBooleanAnythingAsFalse() throws Exception {
        doAssert(getWrapper(), "booleanValue", "anything", false);
    }

    @Test
    public void bindInteger() throws Exception {
        doAssert(getWrapper(), "integerValue", "234", 234);

    }

    @Test
    public void bindIntegerNegative() throws Exception {
        doAssert(getWrapper(), "integerValue", "-234", -234);
    }

    @Test
    public void bindShort() throws Exception {
        doAssert(getWrapper(), "shortValue", "123", (short) 123);
    }

    @Test
    public void bindShortNegative() throws Exception {
        doAssert(getWrapper(), "shortValue", "-123", (short) -123);
    }

    @Test
    public void bindLong() throws Exception {
        doAssert(getWrapper(), "longValue", "234567890123", 234567890123L);
    }

    @Test
    public void bindLongNegative() throws Exception {
        doAssert(getWrapper(), "longValue", "-234567890123", -234567890123L);
    }

    @Test
    public strictfp void bindFloat() throws Exception {
        doAssert(getWrapper(), "floatValue", "23.456789", 23.456789F);
    }

    @Test
    public strictfp void bindFloatNegative() throws Exception {
        doAssert(getWrapper(), "floatValue", "-23.456789", -23.456789F);
    }

    @Test
    public strictfp void bindDouble() throws Exception {
        doAssert(getWrapper(), "doubleValue", "23.4567890123", 23.4567890123);
    }

    @Test
    public strictfp void bindDoubleNegative() throws Exception {
        doAssert(getWrapper(), "doubleValue", "-23.4567890123", -23.4567890123);
    }

    @Test
    public void bindCharacter() throws Exception {
        doAssert(getWrapper(), "characterValue", "e", 'e');
    }

    @Test
    public void bindUnicodeCharacter() throws Exception {
        doAssert(getWrapper(), "characterValue", "ś", 'ś');
    }

    @Test
    public void bindByte() throws Exception {
        doAssert(getWrapper(), "byteValue", "101", (byte) 101);
    }

    @Test
    public void bindByteNegative() throws Exception {
        doAssert(getWrapper(), "byteValue", "-101", (byte) -101);
    }
}
