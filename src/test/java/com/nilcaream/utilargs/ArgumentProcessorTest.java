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

import com.nilcaream.utilargs.core.ArgumentBinder;
import com.nilcaream.utilargs.model.Option;
import com.nilcaream.utilargs.model.Parameter;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * TODO
 * <p/>
 * Krzysztof Smigielski 10/28/12 8:32 PM
 */
public class ArgumentProcessorTest {

    private ArgumentProcessor argumentProcessor;

    @Before
    public void setUp() {
        argumentProcessor = new ArgumentProcessor();
        argumentProcessor.setBinders(new ArrayList<ArgumentBinder>());
    }

    @Test
    public void inputHolders() throws Exception {
        // given
        TestObject testObject = new TestObject();
        String[] args = new String[]{"test"};
        argumentProcessor.setBinders(null);

        // when
        argumentProcessor.initialize(args, testObject);

        // then
        assertThat(argumentProcessor.getArguments()).isEqualTo(args);
        assertThat(argumentProcessor.getWrapper()).isEqualTo(testObject);
        assertThat(argumentProcessor.getOperands()).isEqualTo("test");
    }

    @Test
    public void verifyAllDeclaredParameters() throws Exception {
        // given
        TestObject testObject = new TestObject();
        String[] args = new String[0];
        argumentProcessor.initialize(args, testObject);

        // when
        List<Parameter> parameters = argumentProcessor.getDeclaredParameters();

        // then
        assertThat(parameters).hasSize(8);
        assertParameterState(parameters.get(0), 'a', null);
        assertParameterState(parameters.get(1), 'c', null);
        assertParameterState(parameters.get(2), 'n', null);
        assertParameterState(parameters.get(3), 'o', null);
        assertParameterState(parameters.get(4), 't', null);
        assertParameterState(parameters.get(5), 'v', null);
        assertParameterState(parameters.get(6), 'y', null);
        assertParameterState(parameters.get(7), 'z', null);
    }

    @Test
    public void verifySomeMatchingParameters() throws Exception {
        // given
        TestObject testObject = new TestObject();
        String[] args = "-c 1 -o -z 4 -q 2".split(" ");
        argumentProcessor.initialize(args, testObject);

        // when
        List<Parameter> parameters = argumentProcessor.getParameters();

        // then
        assertThat(parameters).hasSize(3);
        assertParameterState(parameters.get(0), 'c', "1");
        assertParameterState(parameters.get(1), 'o', "");
        assertParameterState(parameters.get(2), 'z', "4");
    }

    @Test
    public void verifyNoParameters() throws Exception {
        // given
        TestObject testObject = new TestObject();
        String[] args = new String[0];

        // when
        argumentProcessor.initialize(args, testObject);
        List<Parameter> parameters = argumentProcessor.getParameters();

        // then
        assertThat(parameters).isNotNull();
        assertThat(parameters).isEmpty();
        assertThat(argumentProcessor.getOperandsIndex()).isEqualTo(0);
        assertThat(argumentProcessor.getOperands()).isEqualTo("");
    }

    @Test
    public void negativeNumbers() throws Exception {
        // given
        TestObject testObject = new TestObject();
        String[] args = "-c -1 -o -2b -z -0.04 -q 2".split(" ");
        argumentProcessor.initialize(args, testObject);

        // when
        List<Parameter> parameters = argumentProcessor.getParameters();

        // then
        assertThat(parameters).hasSize(3);
        assertParameterState(parameters.get(0), 'c', "-1");
        assertParameterState(parameters.get(1), 'o', "-2b");
        assertParameterState(parameters.get(2), 'z', "-0.04");
    }

    @Test
    public void singleString() throws Exception {
        // given
        TestObject testObject = new TestObject();
        String[] args = "-n name".split(" ");

        // when
        argumentProcessor.initialize(args, testObject);
        List<Parameter> parameters = argumentProcessor.getParameters();

        // then
        assertThat(parameters).hasSize(1);
        assertParameterState(parameters.get(0), 'n', "name");
    }

    @Test
    public void twoArguments() throws Exception {
        // given
        TestObject testObject = new TestObject();
        String[] args = "-o 123 -n test".split(" ");

        // when
        argumentProcessor.initialize(args, testObject);
        List<Parameter> parameters = argumentProcessor.getParameters();

        // then
        assertThat(parameters).hasSize(2);
        assertParameterState(parameters.get(0), 'n', "test");
        assertParameterState(parameters.get(1), 'o', "123");
    }

    @Test
    public void verifySimpleOperands() throws Exception {
        // given
        TestObject testObject = new TestObject();
        String[] args = "-o 123 -n test my operands".split(" ");

        // when
        argumentProcessor.initialize(args, testObject);
        List<Parameter> parameters = argumentProcessor.getParameters();

        // then
        assertThat(parameters).hasSize(2);
        assertParameterState(parameters.get(0), 'n', "test");
        assertParameterState(parameters.get(1), 'o', "123");

        assertThat(argumentProcessor.getOperandsIndex()).isEqualTo(4);
        assertThat(argumentProcessor.getOperands()).isEqualTo("my operands");
    }

    @Test
    public void verifyOperandsWithDelimiter() throws Exception {
        // given
        TestObject testObject = new TestObject();
        String[] args = "-n test -v -- my operands -x test".split(" ");

        // when
        argumentProcessor.initialize(args, testObject);
        List<Parameter> parameters = argumentProcessor.getParameters();

        // then
        assertThat(parameters).hasSize(2);
        assertParameterState(parameters.get(0), 'n', "test");
        assertParameterState(parameters.get(1), 'v', "true");

        assertThat(argumentProcessor.getOperandsIndex()).isEqualTo(4);
        assertThat(argumentProcessor.getOperands()).isEqualTo("my operands -x test");
    }

    @Test
    public void verifyOperandsWithBooleanFlag() throws Exception {
        // given
        TestObject testObject = new TestObject();
        String[] args = "-n test -v test operands -x test".split(" ");

        // when
        argumentProcessor.initialize(args, testObject);
        List<Parameter> parameters = argumentProcessor.getParameters();

        // then
        assertThat(parameters).hasSize(2);
        assertParameterState(parameters.get(0), 'n', "test");
        assertParameterState(parameters.get(1), 'v', "true");

        assertThat(argumentProcessor.getOperandsIndex()).isEqualTo(3);
        assertThat(argumentProcessor.getOperands()).isEqualTo("test operands -x test");
    }

    @Test
    public void verifyOperandsWithBooleanTrueFlag() throws Exception {
        // given
        TestObject testObject = new TestObject();
        String[] args = "-n test -v true operands -x test".split(" ");

        // when
        argumentProcessor.initialize(args, testObject);
        List<Parameter> parameters = argumentProcessor.getParameters();

        // then
        assertThat(parameters).hasSize(2);
        assertParameterState(parameters.get(0), 'n', "test");
        assertParameterState(parameters.get(1), 'v', "true");

        assertThat(argumentProcessor.getOperandsIndex()).isEqualTo(3);
        assertThat(argumentProcessor.getOperands()).isEqualTo("true operands -x test");
    }

    @Test
    public void verifyOperandsWithTwoDelimiters() throws Exception {
        // given
        TestObject testObject = new TestObject();
        String[] args = "-n test -v -- my -- operands -x test".split(" ");

        // when
        argumentProcessor.initialize(args, testObject);
        List<Parameter> parameters = argumentProcessor.getParameters();

        // then
        assertThat(parameters).hasSize(2);
        assertParameterState(parameters.get(0), 'n', "test");
        assertParameterState(parameters.get(1), 'v', "true");

        assertThat(argumentProcessor.getOperandsIndex()).isEqualTo(4);
        assertThat(argumentProcessor.getOperands()).isEqualTo("my -- operands -x test");
    }

    @Test
    public void lastValueNotSet() throws Exception {
        // given
        TestObject testObject = new TestObject();
        String[] args = "-o 123 -n".split(" ");

        // when
        argumentProcessor.initialize(args, testObject);
        List<Parameter> parameters = argumentProcessor.getParameters();

        // then
        assertThat(parameters).hasSize(2);
        assertParameterState(parameters.get(0), 'n', "");
        assertParameterState(parameters.get(1), 'o', "123");
    }

    @Test
    public void firstValueNotSet() throws Exception {
        // given
        TestObject testObject = new TestObject();
        String[] args = "-o -n name".split(" ");

        // when
        argumentProcessor.initialize(args, testObject);
        List<Parameter> parameters = argumentProcessor.getParameters();

        // then
        assertThat(parameters).hasSize(2);
        assertParameterState(parameters.get(0), 'n', "name");
        assertParameterState(parameters.get(1), 'o', "");
    }

    @Test
    public void middleValueNotSet() throws Exception {
        // given
        TestObject testObject = new TestObject();
        String[] args = "-c city -o -n name".split(" ");

        // when
        argumentProcessor.initialize(args, testObject);
        List<Parameter> parameters = argumentProcessor.getParameters();

        // then
        assertThat(parameters).hasSize(3);
        assertParameterState(parameters.get(0), 'c', "city");
        assertParameterState(parameters.get(1), 'n', "name");
        assertParameterState(parameters.get(2), 'o', "");
    }

    @Test
    public void allValuesNotSet() throws Exception {
        // given
        TestObject testObject = new TestObject();
        String[] args = "-c -o -n".split(" ");

        // when
        argumentProcessor.initialize(args, testObject);
        List<Parameter> parameters = argumentProcessor.getParameters();

        // then
        assertThat(parameters).hasSize(3);
        assertParameterState(parameters.get(0), 'c', "");
        assertParameterState(parameters.get(1), 'n', "");
        assertParameterState(parameters.get(2), 'o', "");
    }

    @Test
    public void simpleBooleanGroup() throws Exception {
        // given
        TestObject testObject = new TestObject();
        String[] args = "-vy".split(" ");

        // when
        argumentProcessor.initialize(args, testObject);
        List<Parameter> parameters = argumentProcessor.getParameters();

        // then
        assertThat(parameters).hasSize(2);
        assertParameterState(parameters.get(0), 'v', "true");
        assertParameterState(parameters.get(1), 'y', "true");
    }

    @Test
    public void complexBooleanGroup() throws Exception {
        // given
        TestObject testObject = new TestObject();
        String[] args = "-c city -vy -n name".split(" ");

        // when
        argumentProcessor.initialize(args, testObject);
        List<Parameter> parameters = argumentProcessor.getParameters();

        // then
        assertThat(parameters).hasSize(4);
        assertParameterState(parameters.get(0), 'c', "city");
        assertParameterState(parameters.get(1), 'n', "name");
        assertParameterState(parameters.get(2), 'v', "true");
        assertParameterState(parameters.get(3), 'y', "true");
    }

    @Test
    public void booleanGroupWithOperands() throws Exception {
        // given
        TestObject testObject = new TestObject();
        String[] args = "-vy -true true".split(" ");

        // when
        argumentProcessor.initialize(args, testObject);
        List<Parameter> parameters = argumentProcessor.getParameters();

        // then
        assertThat(parameters).hasSize(2);
        assertParameterState(parameters.get(0), 'v', "true");
        assertParameterState(parameters.get(1), 'y', "true");
        assertThat(argumentProcessor.getOperandsIndex()).isEqualTo(1);
        assertThat(argumentProcessor.getOperands()).isEqualTo("-true true");
    }

    @Test
    public void invalidTypesBooleanGroupWithOperands() throws Exception {
        // given
        TestObject testObject = new TestObject();
        String operands = "-vyo true";
        String[] args = operands.split(" ");

        // when
        argumentProcessor.initialize(args, testObject);
        List<Parameter> parameters = argumentProcessor.getParameters();

        // then
        assertThat(parameters).hasSize(0);
        assertThat(argumentProcessor.getOperandsIndex()).isEqualTo(0);
        assertThat(argumentProcessor.getOperands()).isEqualTo(operands);
    }

    @Test
    public void stringPlusInvalidTypesBooleanGroupWithOperands() throws Exception {
        // given
        TestObject testObject = new TestObject();
        String[] args = "-c -vy -vyo true".split(" ");

        // when
        argumentProcessor.initialize(args, testObject);
        List<Parameter> parameters = argumentProcessor.getParameters();

        // then
        assertThat(parameters).hasSize(1);
        assertParameterState(parameters.get(0), 'c', "-vy");
        assertThat(argumentProcessor.getOperandsIndex()).isEqualTo(2);
        assertThat(argumentProcessor.getOperands()).isEqualTo("-vyo true");
    }

    @Test
    public void booleanGroupWithOperandsDelimiter() throws Exception {
        // given
        TestObject testObject = new TestObject();
        String[] args = "-vy -- -a".split(" ");

        // when
        argumentProcessor.initialize(args, testObject);
        List<Parameter> parameters = argumentProcessor.getParameters();

        // then
        assertThat(parameters).hasSize(2);
        assertParameterState(parameters.get(0), 'v', "true");
        assertParameterState(parameters.get(1), 'y', "true");
        assertThat(argumentProcessor.getOperandsIndex()).isEqualTo(2);
        assertThat(argumentProcessor.getOperands()).isEqualTo("-a");
    }

    @Test
    public void operandCharactersOnly() throws Exception {
        // given
        TestObject testObject = new TestObject();
        String[] args = "a b c".split(" ");

        // when
        argumentProcessor.initialize(args, testObject);
        List<Parameter> parameters = argumentProcessor.getParameters();

        // then
        assertThat(parameters).hasSize(0);
        assertThat(argumentProcessor.getOperandsIndex()).isEqualTo(0);
        assertThat(argumentProcessor.getOperands()).isEqualTo("a b c");
    }

    @Test
    public void operandsWithMinusSign() throws Exception {
        // given
        TestObject testObject = new TestObject();
        String[] args = "- a b c".split(" ");

        // when
        argumentProcessor.initialize(args, testObject);
        List<Parameter> parameters = argumentProcessor.getParameters();

        // then
        assertThat(parameters).hasSize(0);
        assertThat(argumentProcessor.getOperandsIndex()).isEqualTo(0);
        assertThat(argumentProcessor.getOperands()).isEqualTo("- a b c");
    }

    @Test
    public void operandAsString() throws Exception {
        // given
        TestObject testObject = new TestObject();
        String[] args = "test".split(" ");

        // when
        argumentProcessor.initialize(args, testObject);
        List<Parameter> parameters = argumentProcessor.getParameters();

        // then
        assertThat(parameters).hasSize(0);
        assertThat(argumentProcessor.getOperandsIndex()).isEqualTo(0);
        assertThat(argumentProcessor.getOperands()).isEqualTo("test");
    }

    private void assertParameterState(Parameter parameter, char option, String argument) {
        assertThat(parameter.getOption().name()).isEqualTo(option);
        assertThat(parameter.getArgument()).isEqualTo(argument);
    }

    private static final class TestObject {
        @Option(name = 'n')
        String name;

        @Option(name = 'c')
        String city;

        @Option(name = 'v')
        Boolean verbose;

        @Option(name = 'y')
        Boolean debug;

        @Option(name = 'a')
        Boolean amber;

        @Option(name = 'o')
        int number;

        @Option(name = 't')
        String optional;

        @Option(name = 'z')
        Integer integer;

        Integer noOptionInteger;

        String noOptionString;
    }
}
