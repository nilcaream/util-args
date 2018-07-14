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
 * Krzysztof Smigielski 1/31/13 10:18 PM
 */
public class BooleanIntegerFunctionalTest extends AbstractFunctionalTest {

    @Test
    public void twoIntegers() throws Exception {
        String args = "-a 1 -b 2 other";
        String result = "a=1,b=2|other";
        verify(args, result);
    }

    @Test
    public void twoIntegersOperandsDelimiter() throws Exception {
        String args = "-a 1 -b 2 -- other";
        String result = "a=1,b=2|other";
        verify(args, result);
    }

    @Test
    public void twoIntegersInvalidValue() throws Exception {
        String args = "-a 1 -b 2a other";
        String result = "a=1|other";
        verify(args, result);
    }

    @Test
    public void twoIntegersInvalidValueOperandsDelimiter() throws Exception {
        String args = "-a 1 -b 2a -- other";
        String result = "a=1|other";
        verify(args, result);
    }

    @Test
    public void twoIntegersBothInvalidValue() throws Exception {
        String args = "-a 1e1 -b 2a other";
        String result = "|other";
        verify(args, result);
    }

    @Test
    public void twoIntegersInvalidFloatValue() throws Exception {
        String args = "-a 1 -b 82.1 other";
        String result = "a=1|other";
        verify(args, result);
    }

    @Test
    public void oneIntegerInvalidOption() throws Exception {
        String args = "-a 1 -c 1 other";
        String result = "a=1|-c 1 other";
        verify(args, result);
    }

    @Test
    public void secondIntegerInvalidOption() throws Exception {
        String args = "-b 1 -i 1 -a 7 other";
        String result = "a=7,b=1|other";
        verify(args, result);
    }

    @Test
    public void secondIntegerInvalidOptionWithOperandDelimiter() throws Exception {
        String args = "-b 1 -i 1 -a 7 -- other";
        String result = "a=7,b=1|other";
        verify(args, result);
    }

    @Test
    public void simpleBooleanAndString() throws Exception {
        String args = "-x -s name";
        String result = "x=true,s=name|";
        verify(args, result);
    }

    @Test
    public void simpleBooleanAndString2() throws Exception {
        String args = "-s name -x";
        String result = "x=true,s=name|";
        verify(args, result);
    }

    @Test
    public void simpleBooleanAndStringWithOperands() throws Exception {
        String args = "-x -s name true";
        String result = "x=true,s=name|true";
        verify(args, result);
    }

    @Test
    public void simpleBooleanAndStringWithOperands2() throws Exception {
        String args = "-s name -x true";
        String result = "x=true,s=name|true";
        verify(args, result);
    }

    @Test
    public void booleanValueAsNumber() throws Exception {
        String args = "-s name -x 1";
        String result = "x=true,s=name|1";
        verify(args, result);
    }

    @Test
    public void booleanValueAsFloat() throws Exception {
        String args = "-s name -x 1.1";
        String result = "x=true,s=name|1.1";
        verify(args, result);
    }

    @Test
    public void simpleBooleanAndStringWithDelimiter() throws Exception {
        String args = "-x -s name -- true x";
        String result = "x=true,s=name|true x";
        verify(args, result);
    }

    @Test
    public void simpleBooleanAndStringWithDelimiter2() throws Exception {
        String args = "-s name -x -- true x";
        String result = "x=true,s=name|true x";
        verify(args, result);
    }

    @Test(expected = AssertionError.class)
    public void missingFieldValue() throws Exception {
        String args = "-a 1 -c 1 other";
        String result = "b=1|-c 1 other";
        verify(args, result);
    }

    @Test(expected = AssertionError.class)
    public void incorrectFieldValue() throws Exception {
        String args = "-a 1 -c 1 other";
        String result = "a=2|-c 1 other";
        verify(args, result);
    }

    @Test(expected = AssertionError.class)
    public void noValuesSet() throws Exception {
        String args = "-a 1 -c 1 other";
        String result = "|-c 1 other";
        verify(args, result);
    }

    @Test(expected = AssertionError.class)
    public void otherValueNotSet() throws Exception {
        String args = "-a 1 -b 1 other";
        String result = "a=1|other";
        verify(args, result);
    }

    @Override
    protected Object createTestObject() {
        return new TestObject();
    }

    private static final class TestObject {
        @Option(name = 'a')
        Integer fa;

        @Option(name = 'b')
        Integer fb;

        @Option(name = 's')
        String fs;

        @Option(name = 'x')
        Boolean fx;

        @Option(name = 'z')
        Boolean fz;
    }
}
