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

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.Optional.ofNullable;

/**
 * Base all-in-one arguments binder.
 */
public class UtilArgs {

    private final ArgumentsParser parser = new ArgumentsParser();
    private final ValuesSelector selector = new ValuesSelector();
    private final Binder binder = new Binder();
    private boolean failFast = true;

    private String[] args;
    private Object target;
    private String operands;

    /**
     * Performs binding on a target object by using default binding settings.
     *
     * @param args   arguments, usually from application main method.
     * @param target target object; not null.
     * @return stateful UtilArgs instance.
     */
    public static UtilArgs bind(String[] args, Object target) {
        return create(args, target).bind();
    }

    /**
     * Initializes UtilArgs with arguments and target object. Does not perform binding.
     *
     * @param args   arguments, usually from application main method.
     * @param target target object; not null.
     * @return stateful UtilArgs instance.
     */
    public static UtilArgs create(String[] args, Object target) {
        return new UtilArgs().initialize(args, target);
    }

    private UtilArgs initialize(String[] args, Object target) {
        this.args = ofNullable(args).orElse(new String[]{});
        this.target = ofNullable(target).orElseThrow(() -> new IllegalArgumentException("Target cannot be null"));
        return this;
    }

    /**
     * Binds arguments to a target object. Errors out only with UtilArgsException
     * (for known binding errors) if failFast is true.
     *
     * @return stateful UtilArgs instance.
     */
    public UtilArgs bind() {
        Map<String, List<String>> arguments = parser.parse(args);
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

    /**
     * Gets operands i.e. arguments that follow -- option.
     *
     * @return operands or empty string; non-null.
     */
    public String getOperands() {
        return operands;
    }

    /**
     * Disables failing fast. Results in bind method failing with UtilArgsException.
     *
     * @return stateful UtilArgs instance.
     */
    public UtilArgs disableFailFast() {
        failFast = false;
        return this;
    }

    /**
     * Disables using first match when binding values are ambiguous.
     *
     * @return stateful UtilArgs instance.
     */
    public UtilArgs disableUseFirst() {
        binder.withUseFirst(false);
        return this;
    }

    /**
     * Disables using last match when binding values are ambiguous.
     *
     * @return stateful UtilArgs instance.
     */
    public UtilArgs disableUseLast() {
        binder.withUseLast(false);
        return this;
    }

    /**
     * Sets custom value mapper.
     *
     * @param mapper mapper instance.
     * @return stateful UtilArgs instance.
     */
    public UtilArgs withMapper(Mapper mapper) {
        binder.withMapper(mapper);
        return this;
    }
}
