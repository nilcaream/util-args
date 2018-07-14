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

package com.nilcaream.utilargs.model;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * TODO
 * <p/>
 * Krzysztof Smigielski 2/2/13 11:16 PM
 */
public class ParameterTest {

    @Test
    public void standardCompare() throws Exception {
        // given
        Parameter a = new Parameter();
        Parameter b = new Parameter();
        Parameter c = new Parameter();

        // when
        a.setOption(TestObject.class.getDeclaredField("optionA").getAnnotation(Option.class));
        b.setOption(TestObject.class.getDeclaredField("optionB").getAnnotation(Option.class));
        c.setOption(TestObject.class.getDeclaredField("optionC").getAnnotation(Option.class));

        // then
        assertThat(a.compareTo(a)).isEqualTo(0);
        assertThat(a.compareTo(b)).isEqualTo(-1);
        assertThat(a.compareTo(c)).isEqualTo(-2);

        assertThat(c.compareTo(a)).isEqualTo(2);
        assertThat(c.compareTo(b)).isEqualTo(1);
        assertThat(c.compareTo(c)).isEqualTo(0);
    }

    @Test
    public void compareWithNullOption() throws Exception {
        // given
        Parameter a = new Parameter();
        Parameter b = new Parameter();

        // when
        a.setOption(TestObject.class.getDeclaredField("optionA").getAnnotation(Option.class));
        b.setOption(null);

        // then
        assertThat(a.compareTo(b)).isEqualTo(0);
        assertThat(b.compareTo(a)).isEqualTo(0);
    }

    @Test
    public void compareWithNullObject() throws Exception {
        // given
        Parameter a = new Parameter();
        Parameter b = null;

        // when
        a.setOption(TestObject.class.getDeclaredField("optionA").getAnnotation(Option.class));

        // then
        assertThat(a.compareTo(b)).isEqualTo(0);
    }

    private static final class TestObject {
        @Option(name = 'A')
        int optionA;

        @Option(name = 'B')
        int optionB;

        @Option(name = 'C')
        int optionC;
    }
}
