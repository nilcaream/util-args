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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Command line string argument parser.
 */
public class ArgumentsParser {

    /**
     * Maps array of command line arguments to a map of values.
     *
     * @param args arguments, usually from application main method.
     * @return map of argument to list of values.
     */
    public Map<String, List<String>> parse(String... args) {
        Map<String, List<String>> results = new HashMap<>();
        for (int i = 0, length = args.length; i < length; i++) {
            String key = args[i];
            String value = get(args, i + 1);
            if (key.equals("--")) {
                if (!value.isEmpty()) {
                    String operands = String.join(" ", Arrays.copyOfRange(args, i + 1, length));
                    add(results, key, operands);
                }
                break;
            } else if (key.startsWith("--") && key.length() > 2) {
                add(results, key.substring(2), value);
            } else if (key.startsWith("-") && key.length() == 2 && (value.isEmpty() || value.equals("--"))) {
                add(results, key.substring(1), "true");
            } else if (key.startsWith("-") && key.length() == 2) {
                add(results, key.substring(1), value);
            } else if (key.startsWith("-") && key.length() > 2) {
                for (String flag : key.substring(1).split("")) {
                    add(results, flag, "true");
                }
            }
        }
        return results;
    }

    private void add(Map<String, List<String>> results, String key, String value) {
        List<String> values = results.computeIfAbsent(key, k -> new ArrayList<>());
        if (!values.contains(value)) {
            values.add(value);
        }
    }

    private String get(String[] args, int index) {
        if (index >= args.length) {
            return "";
        } else {
            return args[index];
        }
    }
}
