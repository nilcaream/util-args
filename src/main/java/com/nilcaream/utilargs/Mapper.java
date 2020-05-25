package com.nilcaream.utilargs;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class Mapper {

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
