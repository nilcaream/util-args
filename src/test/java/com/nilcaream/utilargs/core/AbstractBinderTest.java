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

import java.lang.reflect.Field;

import static org.fest.assertions.Assertions.assertThat;

/**
 * TODO
 * <p/>
 * Krzysztof Smigielski 1/27/13 1:44 PM
 */
public abstract class AbstractBinderTest {

    protected ArgumentBinder binder;

    protected void doAssert(Object wrapper, String fieldName, String actual, Object expected) throws Exception {
        // given
        Parameter parameter = createFrom(wrapper, fieldName, actual);

        // when
        binder.bind(parameter, wrapper);

        // then
        assertThat(wrapper.getClass().getDeclaredField(fieldName).get(wrapper)).isEqualTo(expected);
    }

    protected Parameter createFrom(Object wrapper, String fieldName, String argument) throws NoSuchFieldException {
        Parameter parameter = new Parameter();
        Field field = wrapper.getClass().getDeclaredField(fieldName);
        parameter.setField(field);
        parameter.setArgument(argument);
        return parameter;
    }
}
