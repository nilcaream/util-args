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

import org.junit.Before;

/**
 * TODO
 * <p/>
 * Krzysztof Smigielski 10/30/12 7:50 PM
 */
public class WrappersBinderTest extends AbstractStaticValueOfBinderTest {

    @Before
    public void setUp() {
        binder = new StaticValueOfBinder();
    }

    @Override
    protected Object getWrapper() {
        return new TestObject();
    }

    private static class TestObject {
        Integer integerValue;
        Short shortValue;
        Byte byteValue;
        Character characterValue;
        Long longValue;
        Float floatValue;
        Double doubleValue;
        Boolean booleanValue;
    }
}
