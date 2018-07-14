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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

/**
 * Binder that uses constructor with String argument.
 * <p/>
 * Krzysztof Smigielski 10/30/12 7:49 PM
 */
public class StringConstructorBinder implements ArgumentBinder {

    @Override
    public void bind(Parameter parameter, Object wrapper) throws Exception {
        Field field = parameter.getField();
        Constructor constructor = field.getType().getConstructor(String.class);
        Object value = constructor.newInstance(parameter.getArgument());
        field.setAccessible(true);
        field.set(wrapper, value);
    }
}
