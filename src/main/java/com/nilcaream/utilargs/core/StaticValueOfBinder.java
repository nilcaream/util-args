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
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Binder that uses static valueOf method. Suitable for primitives types and their wrapper classes.
 * <p/>
 * Krzysztof Smigielski 10/30/12 7:47 PM
 */
public class StaticValueOfBinder implements ArgumentBinder {

    @Override
    public void bind(Parameter parameter, Object wrapper) throws Exception {
        Field field = parameter.getField();
        Class<?> fieldType = getType(field);
        Class<?> parameterType = getParameterType(field);
        Method valueOf = fieldType.getMethod("valueOf", parameterType);
        if (Modifier.isStatic(valueOf.getModifiers())) {
            Object argument = getArgument(parameter);
            Object value = valueOf.invoke(null, argument);
            field.setAccessible(true);
            field.set(wrapper, value);
        } else {
            throw new NoSuchMethodException("Method valueOf(String) is not static");
        }
    }

    private Class<?> getType(Field field) throws ClassNotFoundException {
        Class<?> type = field.getType();
        if (type.isPrimitive()) {
            type = Class.forName("java.lang." + getWrappingClassName(type.getName()));
        }
        return type;
    }

    private String getWrappingClassName(String primitiveClassName) {
        String result;
        if (primitiveClassName.equals("int")) {
            result = "Integer";
        } else if (primitiveClassName.equals("char")) {
            result = "Character";
        } else {
            result = toCamelCase(primitiveClassName);
        }
        return result;
    }

    private String toCamelCase(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    private Class<?> getParameterType(Field field) {
        Class<?> type = String.class;
        if (isCharacter(field)) {
            type = char.class;
        }
        return type;
    }

    private Object getArgument(Parameter parameter) {
        Object argument = parameter.getArgument();
        if (isCharacter(parameter.getField())) {
            argument = parameter.getArgument().charAt(0);
        }
        return argument;
    }

    private boolean isCharacter(Field field) {
        return field.getType().equals(char.class) || field.getType().equals(Character.class);
    }
}
