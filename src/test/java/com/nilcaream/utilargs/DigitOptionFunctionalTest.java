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

/**
 * TODO
 * <p/>
 * Krzysztof Smigielski 2/1/13 8:54 PM
 */
public class DigitOptionFunctionalTest extends AbstractFunctionalTest {

    @Test
    public void singleOption() throws Exception {
        String args = "-1 2";
        String result = "1=2|";
        verify(args, result);
    }

    @Test
    public void twoOptions() throws Exception {
        String args = "-1 2 -2 3";
        String result = "1=2,2=3|";
        verify(args, result);
    }

    @Test
    public void twoOptionsNegativeValues() throws Exception {
        String args = "-1 -2 -2 -1";
        String result = "1=-2,2=-1|";
        verify(args, result);
    }

    @Test
    public void singleBoolean() throws Exception {
        String args = "-8";
        String result = "8=true|";
        verify(args, result);
    }

    @Test
    public void digitAndBoolean() throws Exception {
        String args = "-1 -3 -8";
        String result = "1=-3,8=true|";
        verify(args, result);
    }

    @Test
    public void booleanAndDigit() throws Exception {
        String args = "-8 -1 -8";
        String result = "1=-8,8=true|";
        verify(args, result);
    }

    @Test
    public void booleanAndInvalidOptionDigit() throws Exception {
        String args = "-8 -3 -5 2";
        String result = "8=true|-3 -5 2";
        verify(args, result);
    }

    @Test
    public void booleanAndDigitWithOperandDelimiter() throws Exception {
        String args = "-8 -- -1 -5 2";
        String result = "8=true|-1 -5 2";
        verify(args, result);
    }

    @Test
    public void booleanOptionAsIntegerValue() throws Exception {
        String args = "-1 -8 -2 -8";
        String result = "1=-8,2=-8|";
        verify(args, result);
    }

    @Test(expected = AssertionError.class)
    public void invalidOperand() throws Exception {
        String args = "-x 1";
        String result = "x=1|o";
        verify(args, result);
    }

    @Test(expected = AssertionError.class)
    public void missingFieldValue() throws Exception {
        String args = "-x 1 -v 1 other";
        String result = "z=1|-v 1 other";
        verify(args, result);
    }

    @Test(expected = AssertionError.class)
    public void incorrectFieldValue() throws Exception {
        String args = "-x 1 -z 1 other";
        String result = "x=1,z=2|other";
        verify(args, result);
    }

    @Test(expected = AssertionError.class)
    public void noValuesSet() throws Exception {
        String args = "-x 1 -z 1 other";
        String result = "|1 other";
        verify(args, result);
    }

    @Test(expected = AssertionError.class)
    public void otherValueNotSet() throws Exception {
        String args = "-x 1 -z 1 other";
        String result = "x=1|other";
        verify(args, result);
    }

    @Override
    protected Object createTestObject() {
        return new TestObject();
    }

    private static final class TestObject {
        @Option(name = '1')
        Integer f1;

        @Option(name = '2')
        Integer f2;

        @Option(name = '8')
        Boolean f8;

        @Option(name = 'x')
        Integer fx;

        @Option(name = 'z')
        Integer fz;
    }
}
