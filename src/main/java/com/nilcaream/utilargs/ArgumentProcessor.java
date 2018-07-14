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

package com.nilcaream.utilargs;

import com.nilcaream.utilargs.core.ArgumentBinder;
import com.nilcaream.utilargs.model.Option;
import com.nilcaream.utilargs.model.Parameter;

import java.lang.reflect.Field;
import java.util.*;

/**
 * POSIX-style argument parser. Contains minimal subset of POSIX-1.2008 standard.
 * <p/>
 * This class is stateful and is not thread-safe.
 * <p/>
 * Krzysztof Smigielski 10/28/12 7:31 PM
 */
public class ArgumentProcessor {

    private String[] arguments;
    private Object wrapper;

    private Map<Character, Parameter> optionNameToParameter = new HashMap<Character, Parameter>();
    private List<ArgumentBinder> binders = new ArrayList<ArgumentBinder>();
    private int operandsIndex;

    public ArgumentProcessor() {
    }

    /**
     * Initiates argument resolution and binding procedure.
     *
     * @param arguments command line argument
     * @param wrapper   option wrapper object
     */
    public final void initialize(String[] arguments, Object wrapper) {
        operandsIndex = 0;
        optionNameToParameter.clear();

        this.arguments = arguments;
        this.wrapper = wrapper;

        resolveWrapperFields();
        processArguments();
        bindValues();
    }

    private void resolveWrapperFields() {
        Field[] fields = wrapper.getClass().getDeclaredFields();
        for (Field field : fields) {
            Parameter parameter = createArgument(field);
            if (parameter != null) {
                optionNameToParameter.put(parameter.getOption().name(), parameter);
            }
        }
    }

    private Parameter createArgument(Field field) {
        Parameter parameter = null;
        Option option = field.getAnnotation(Option.class);
        if (option != null) {
            parameter = new Parameter();
            parameter.setOption(option);
            parameter.setField(field);
        }
        return parameter;
    }

    private void processArguments() {
        if (areArgumentsAvailable()) {
            for (int index = 0; index < arguments.length; ) {
                Parameter parameter = getParameterByKey(arguments[index]);
                String key = arguments[index];
                String value = getNextArgument(index);

                index += 1;
                if (parameter != null) {
                    if (isBoolean(parameter)) {
                        parameter.setArgument("true");
                    } else if (isOption(value) && !isNumber(value)) {
                        parameter.setArgument("");
                    } else {
                        parameter.setArgument(value);
                        index += 1;
                    }

                    if (isEndOfOptionsDelimiter(index)) {
                        operandsIndex = index + 1;
                        break;
                    } else {
                        operandsIndex = index;
                    }
                } else if (isBooleanGroup(key)) {
                    for (char option : key.substring(1).toCharArray()) {
                        parameter = optionNameToParameter.get(option);
                        parameter.setArgument("true");
                    }

                    if (isEndOfOptionsDelimiter(index)) {
                        operandsIndex = index + 1;
                        break;
                    } else {
                        operandsIndex = index;
                    }
                }
            }
        }
    }

    private boolean areArgumentsAvailable() {
        return arguments.length != 0 && arguments[0].startsWith("-") && !optionNameToParameter.isEmpty();
    }

    private Parameter getParameterByKey(String key) {
        Parameter parameter = null;
        if (isOption(key)) {
            parameter = optionNameToParameter.get(key.charAt(1));
        }
        return parameter;
    }

    private boolean isOption(String key) {
        return key.length() == 2 && key.startsWith("-");
    }

    private String getNextArgument(int index) {
        String nextArg = "";
        if (index + 1 < arguments.length) {
            nextArg = arguments[index + 1];
        }
        return nextArg;
    }

    private boolean isBoolean(Parameter parameter) {
        return parameter.getField().getType().getSimpleName().equalsIgnoreCase("boolean");
    }

    private boolean isNumber(String value) {
        boolean isNumber = true;
        try {
            Double.parseDouble(value);
        } catch (Exception e) {
            isNumber = false;
        }
        return isNumber;
    }

    private boolean isEndOfOptionsDelimiter(int index) {
        return index < arguments.length && arguments[index].equals("--");
    }

    private boolean isBooleanGroup(String booleanGroup) {
        boolean isBooleanGroup = false;
        if (booleanGroup.startsWith("-") && booleanGroup.length() > 2) {
            isBooleanGroup = true;
            for (char option : booleanGroup.substring(1).toCharArray()) {
                Parameter parameter = optionNameToParameter.get(option);
                if (parameter == null || !isBoolean(parameter)) {
                    isBooleanGroup = false;
                }
            }
        }
        return isBooleanGroup;
    }

    private void bindValues() {
        for (Parameter parameter : getParameters()) {
            bindValue(parameter);
        }
    }

    private void bindValue(Parameter parameter) {
        for (ArgumentBinder binder : binders) {
            try {
                binder.bind(parameter, wrapper);
                break;
            } catch (Exception e) {
                // ignore and keep looking
            }
        }
    }

    public String getOperands() {
        StringBuilder operands = new StringBuilder();
        if (operandsIndex < arguments.length) {
            for (int index = operandsIndex; index < arguments.length; index++) {
                operands.append(arguments[index]).append(" ");
            }
        }
        return operands.toString().trim();
    }

    /**
     * Gets list of all parameters found in wrapper object.
     *
     * @return not-null list sorted by an option name.
     */
    public List<Parameter> getDeclaredParameters() {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.addAll(optionNameToParameter.values());
        Collections.sort(parameters);
        return parameters;
    }

    /**
     * Gets list of parameters found in wrapper class that were also present in arguments.
     *
     * @return not-null list sorted by an option name.
     */
    public final List<Parameter> getParameters() {
        List<Parameter> parameters = new ArrayList<Parameter>();
        for (Parameter parameter : optionNameToParameter.values()) {
            if (parameter.getArgument() != null) {
                parameters.add(parameter);
            }
        }
        Collections.sort(parameters);
        return parameters;
    }

    public String[] getArguments() {
        return arguments;
    }

    public Object getWrapper() {
        return wrapper;
    }

    public int getOperandsIndex() {
        return operandsIndex;
    }

    public List<ArgumentBinder> getBinders() {
        return binders;
    }

    public void setBinders(List<ArgumentBinder> binders) {
        this.binders = binders;
    }
}
