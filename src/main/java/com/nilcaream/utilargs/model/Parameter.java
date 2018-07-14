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

package com.nilcaream.utilargs.model;

import java.lang.reflect.Field;

/**
 * Command line argument wrapper with associated argument, option and wrapping object field.
 * <p/>
 * Krzysztof Smigielski 10/28/12 7:39 PM
 */
public class Parameter implements Comparable<Parameter> {

    private Option option;
    private String argument;
    private Field field;

    public Option getOption() {
        return option;
    }

    public void setOption(Option option) {
        this.option = option;
    }

    public String getArgument() {
        return argument;
    }

    public void setArgument(String argument) {
        this.argument = argument;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    @Override
    public int compareTo(Parameter o) {
        if (o == null || o.option == null || option == null) {
            return 0;
        }
        return option.name() - o.option.name();
    }
}
