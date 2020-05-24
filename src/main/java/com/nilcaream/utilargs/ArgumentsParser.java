package com.nilcaream.utilargs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArgumentsParser {

    public Map<String, List<String>> process(String... args) {
        Map<String, List<String>> results = new HashMap<>();
        for (int i = 0, length = args.length; i < length; i++) {
            String key = args[i];
            String value = get(args, i + 1);
            if (key.startsWith("--") && key.length() == 2) {
                if (!value.isEmpty()) {
                    String operands = String.join(" ", Arrays.copyOfRange(args, i + 1, length));
                    add(results, key, operands);
                }
                break;
            } else if (key.startsWith("--") && key.length() > 2) {
                add(results, key.substring(2), value);
            } else if (key.startsWith("-") && key.length() > 1) {
                add(results, key.substring(1), value);
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
