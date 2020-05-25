package com.nilcaream.utilargs;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.Optional.ofNullable;

public class UtilArgs {

    private final ArgumentsParser parser = new ArgumentsParser();
    private final ValuesSelector selector = new ValuesSelector();
    private final Binder binder = new Binder();
    private boolean failFast = true;

    private String[] args;
    private Object target;
    private String operands;

    public static UtilArgs bind(String[] args, Object target) {
        return create(args, target).bind();
    }

    public static UtilArgs create(String[] args, Object target) {
        return new UtilArgs().initialize(args, target);
    }

    private UtilArgs initialize(String[] args, Object target) {
        this.args = ofNullable(args).orElse(new String[]{});
        this.target = ofNullable(target).orElseThrow(() -> new IllegalArgumentException("Target cannot be null"));
        return this;
    }

    public UtilArgs bind() {
        Map<String, List<String>> arguments = parser.process(args);
        for (Field field : target.getClass().getDeclaredFields()) {
            Option option = field.getAnnotation(Option.class);
            if (option != null) {
                try {
                    binder.bind(target, field, selector.select(arguments, option));
                } catch (IllegalAccessException e) {
                    if (failFast) {
                        throw new UtilArgsException("Binding failed for " + field.getName(), e);
                    }
                } catch (UtilArgsException e) {
                    if (failFast) {
                        throw e;
                    }
                }
            }
        }
        operands = arguments.getOrDefault("--", Collections.singletonList("")).get(0);
        return this;
    }

    public String getOperands() {
        return operands;
    }

    public UtilArgs disableFailFast() {
        failFast = false;
        return this;
    }

    public UtilArgs disableUseFirst() {
        binder.withUseFirst(false);
        return this;
    }

    public UtilArgs disableUseLast() {
        binder.withUseLast(false);
        return this;
    }

    public UtilArgs withMapper(Mapper mapper) {
        binder.withMapper(mapper);
        return this;
    }
}
