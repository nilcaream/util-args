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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.Optional.ofNullable;

/**
 * Base all-in-one arguments binder.
 */
public class UtilArgs {

    private final ArgumentsParser parser = new ArgumentsParser();
    private final ValuesSelector selector = new ValuesSelector();
    private final OperandsResolver operandsResolver = new OperandsResolver();
    private final Binder binder = new Binder();
    private boolean failFast = true;

    private String[] args;
    private Object[] targets;
    private String operands;

    private UtilArgs() {
        // use static factory methods
    }

    /**
     * Performs binding on a target object by using default binding settings.
     *
     * @param args    arguments, usually from application main method.
     * @param targets target objects; not null; not empty.
     * @return stateful UtilArgs instance.
     */
    public static UtilArgs bind(String[] args, Object... targets) {
        return create(args, targets).bind();
    }

    /**
     * Initializes UtilArgs with arguments and target object. Does not perform binding.
     *
     * @param args    arguments, usually from application main method.
     * @param targets target objects; not null; non empty.
     * @return stateful UtilArgs instance.
     */
    public static UtilArgs create(String[] args, Object... targets) {
        return new UtilArgs().initialize(args, targets);
    }

    private UtilArgs initialize(String[] args, Object... targets) {
        this.args = ofNullable(args).orElse(new String[]{});
        this.targets = ofNullable(targets).orElseThrow(() -> new IllegalArgumentException("Targets cannot be null"));
        if (targets.length == 0 || Arrays.stream(targets).anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("None of the targets can be null");
        }
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

        for (Object target : targets) {
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
        }

        operands = arguments.getOrDefault("--", Collections.singletonList("")).get(0);
        if (operands.isEmpty()) {
            operands = operandsResolver.resolve(args, targets);
        }
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
