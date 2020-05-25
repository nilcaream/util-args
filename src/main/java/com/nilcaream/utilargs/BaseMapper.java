/*
 * Copyright 2020 Krzysztof Smigielski
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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Reflection-based String value to specific type mapper. This implementation supports using
 * static valueOf(String) method or single argument String constructor if available. Should not
 * fail if these options are not available. Maps most common cases like String (trivial mapping),
 * primitive and boxed types (primitive type wrappers).
 */
public class BaseMapper implements Mapper {

    /**
     * Maps provided value to a specific class instance.
     *
     * @param value String value.
     * @param cls   Target class.
     * @param <T>   Target type.
     * @return Target class instance or null.
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T map(String value, Class<T> cls) {
        T result = null;
        if (cls.equals(String.class)) {
            result = (T) value;
        }
        if (result == null) {
            result = tryStaticValueOf(cls, value);
        }
        if (result == null) {
            result = tryStringConstructor(cls, value);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private <T> T tryStaticValueOf(Class<T> cls, String value) {
        try {
            Class<?> fieldType = getType(cls);
            Class<?> parameterType = getParameterType(cls);
            Method valueOf = fieldType.getMethod("valueOf", parameterType);
            Object argument = getArgument(cls, value);
            if (Modifier.isStatic(valueOf.getModifiers()) && argument != null) {
                return (T) valueOf.invoke(null, argument);
            }
        } catch (InvocationTargetException | ClassNotFoundException | IllegalAccessException | NoSuchMethodException ignore) {
            // ignored;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private <T> T tryStringConstructor(Class<T> cls, String value) {
        try {
            // noinspection rawtypes
            return (T) ((Constructor) cls.getConstructor(String.class)).newInstance(value);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ignore) {
            // ignored;
        }
        return null;
    }

    private Object getArgument(Class<?> cls, String value) {
        Object result = null;
        if (isCharacter(cls)) {
            if (value.length() == 1) {
                result = value.charAt(0);
            }
        } else {
            result = value;
        }
        return result;
    }

    private Class<?> getType(Class<?> type) throws ClassNotFoundException {
        if (type.isPrimitive()) {
            return Class.forName("java.lang." + getWrappingClassName(type.getName()));
        } else {
            return type;
        }
    }

    private String getWrappingClassName(String primitiveClassName) {
        String result;
        switch (primitiveClassName) {
            case "int":
                result = "Integer";
                break;
            case "char":
                result = "Character";
                break;
            default:
                result = toCamelCase(primitiveClassName);
                break;
        }
        return result;
    }

    private String toCamelCase(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    private Class<?> getParameterType(Class<?> type) {
        if (isCharacter(type)) {
            return char.class;
        } else {
            return String.class;
        }
    }

    private boolean isCharacter(Class<?> type) {
        return type.equals(char.class) || type.equals(Character.class);
    }
}
