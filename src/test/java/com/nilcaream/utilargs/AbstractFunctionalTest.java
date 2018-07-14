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

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import static org.fest.assertions.Assertions.assertThat;

/**
 * TODO
 * <p/>
 * Krzysztof Smigielski 2/1/13 8:50 PM
 */
public abstract class AbstractFunctionalTest {

    protected abstract Object createTestObject();

    protected void verify(String commandLineArguments, String expected) throws Exception {
        // given
        Object wrapper = createTestObject();
        String[] expectedEntry = expected.split("\\|");
        String expectedArguments = expectedEntry[0];
        String expectedOperands = "";
        if (expectedEntry.length > 1) {
            expectedOperands = expectedEntry[1];
        }
        Set<String> modifiedFields = new HashSet<String>();

        // when
        UtilArgs ua = new UtilArgs(commandLineArguments.split(" "), wrapper);

        // then
        assertThat(ua.getOperands()).isEqualTo(expectedOperands);

        for (String expectedArgument : expectedArguments.split(",")) {
            if (expectedArgument.isEmpty()) {
                break;
            }
            String[] expectedArgumentEntry = expectedArgument.split("=");
            String fieldName = "f" + expectedArgumentEntry[0];
            String expectedValue = expectedArgumentEntry[1];
            Object actualValue = wrapper.getClass().getDeclaredField(fieldName).get(wrapper);
            assertThat(actualValue).isNotNull();
            assertThat(actualValue.toString()).isEqualTo(expectedValue);
            modifiedFields.add(fieldName);
        }
        for (Field field : wrapper.getClass().getDeclaredFields()) {
            if (!modifiedFields.contains(field.getName())) {
                assertThat(field.get(wrapper)).isNull();
            }
        }
    }

}
