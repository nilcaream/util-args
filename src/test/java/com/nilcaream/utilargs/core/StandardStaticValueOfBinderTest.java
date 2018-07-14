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

import com.nilcaream.utilargs.model.Option;
import org.junit.Before;
import org.junit.Test;

/**
 * TODO
 * <p/>
 * Krzysztof Smigielski 2/2/13 10:52 PM
 */
public class StandardStaticValueOfBinderTest extends AbstractBinderTest {

    @Before
    public void setUp() {
        binder = new StaticValueOfBinder();
    }

    @Test
    public void sanityCheck() throws Exception {
        doAssert(ValidObject.instance, "field", "test", ValidObject.instance);
    }

    @Test(expected = NoSuchMethodException.class)
    public void nonStaticValueOf() throws Exception {
        doAssert(NonStaticValueOf.instance, "field", "test", null);
    }

    @Test(expected = NoSuchMethodException.class)
    public void nonPublicValueOf() throws Exception {
        doAssert(NonPublicValueOf.instance, "field", "test", null);
    }

    private static class ValidObject {
        static ValidObject instance = new ValidObject();

        @Option(name = 'f')
        ValidObject field;

        public static ValidObject valueOf(String s) {
            return instance;
        }
    }

    private static class NonStaticValueOf {
        static NonStaticValueOf instance = new NonStaticValueOf();

        @Option(name = 'f')
        NonStaticValueOf field;

        public NonStaticValueOf valueOf(String s) {
            return instance;
        }
    }

    private static class NonPublicValueOf {
        static NonPublicValueOf instance = new NonPublicValueOf();

        @Option(name = 'f')
        NonPublicValueOf field;

        private static NonPublicValueOf valueOf(String s) {
            return instance;
        }
    }
}
