package com.nilcaream.utilargs;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Binder {

    private boolean useFirst = true;
    private boolean useLast = true;
    private Mapper mapper = new Mapper();

    public Object bind(Object target, Field field, List<String> values) throws IllegalAccessException {
        Object value = process(field.getType(), values);
        if (value != null) {
            field.setAccessible(true);
            field.set(target, value);
        }
        return null;
    }

    private Object process(Class<?> cls, List<String> values) {
        if (values == null || values.isEmpty()) {
            return null;
        } else if (List.class.isAssignableFrom(cls)) {
            return values;
        } else if (Set.class.isAssignableFrom(cls)) {
            return new LinkedHashSet<>(values);
        } else if (cls.isArray()) {
            Class<?> type = cls.getComponentType();
            Object array = Array.newInstance(type, values.size());
            for (int i = 0, size = values.size(); i < size; i++) {
                Array.set(array, i, ensureType(type, values.get(i)));
            }
            return array;
        } else {
            return ensureType(cls, selectValue(values));
        }
    }

    private boolean isBoolean(Class<?> cls) {
        return cls.equals(boolean.class) || cls.equals(Boolean.class);
    }

    private Object ensureType(Class<?> type, String value) {
        Object result = mapper.map(value, type);
        if (result == null) {
            throw new UtilArgsException("Cannot map " + value + " to " + type.getName());
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

    public Binder withUseFirst(boolean useFirst) {
        this.useFirst = useFirst;
        return this;
    }

    public Binder withUseLast(boolean useLast) {
        this.useLast = useLast;
        return this;
    }

    public Binder withMapper(Mapper mapper) {
        this.mapper = mapper;
        return this;
    }
}
