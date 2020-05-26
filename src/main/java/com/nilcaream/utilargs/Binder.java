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

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Reflection-based object fields setter.
 */
public class Binder {

    private boolean useFirst = true;
    private boolean useLast = true;
    private Mapper mapper = new BaseMapper();

    /**
     * Sets field value on a target object based on list of values.
     *
     * @param target target object.
     * @param field  object field.
     * @param values list of values.
     * @throws IllegalAccessException thrown if reflection field setting fails.
     */
    public void bind(Object target, Field field, List<String> values) throws IllegalAccessException {
        Object value = resolve(values, field.getType());
        if (value != null) {
            field.setAccessible(true);
            field.set(target, value);
        }
    }

    private Object resolve(List<String> values, Class<?> cls) {
        if (values == null || values.isEmpty()) {
            return null;
        } else if (isBoolean(cls)) {
            return true;
        } else if (List.class.isAssignableFrom(cls)) {
            return values;
        } else if (Set.class.isAssignableFrom(cls)) {
            return new LinkedHashSet<>(values);
        } else if (cls.isArray()) {
            Class<?> type = cls.getComponentType();
            Object array = Array.newInstance(type, values.size());
            for (int i = 0, size = values.size(); i < size; i++) {
                Array.set(array, i, ensureType(values.get(i), type));
            }
            return array;
        } else {
            return ensureType(selectValue(values), cls);
        }
    }

    private boolean isBoolean(Class<?> cls) {
        return cls.equals(boolean.class) || cls.equals(Boolean.class);
    }

    private Object ensureType(String value, Class<?> cls) {
        Object result = mapper.map(value, cls);
        if (result == null) {
            throw new UtilArgsException("Cannot map " + value + " to " + cls.getName());
        } else {
            return result;
        }
    }

    private String selectValue(List<String> values) {
        if (useFirst) {
            return values.get(0);
        } else if (useLast) {
            return values.get(values.size() - 1);
        } else {
            throw new UtilArgsException("Cannot select single value from [" + String.join(",", values) + "]");
        }
    }

    /**
     * Select first of values in case of multiple values were provided. Setting both useFist and useLast
     * to false will result in an error in case of multiple values binding to a non-collection field.
     *
     * @param useFirst enable / disable.
     * @return binder object.
     */
    public Binder withUseFirst(boolean useFirst) {
        this.useFirst = useFirst;
        return this;
    }

    /**
     * Select last of values in case of multiple values were provided. Works only if useFirst is false.
     * Setting both useFist and useLast to false will result in an error in case of multiple values binding
     * to a non-collection field.
     *
     * @param useLast enable / disable.
     * @return binder object.
     */
    public Binder withUseLast(boolean useLast) {
        this.useLast = useLast;
        return this;
    }

    /**
     * Use custom value mapper.
     *
     * @param mapper mapper instance.
     * @return binder object.
     */
    public Binder withMapper(Mapper mapper) {
        this.mapper = mapper;
        return this;
    }
}
