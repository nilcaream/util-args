package com.nilcaream.utilargs;

import java.util.*;

public class ArgumentProcessor2 {

    public Map<String, List<String>> process(String... args) {
        Map<String, List<String>> results = new HashMap<>();
        for (int i = 0, length = args.length; i < length; i++) {
            String key = args[i];
            String value = get(args, i + 1);
            if (key.startsWith("--") && key.length() == 2 && !value.isEmpty()) {
                String operands = String.join(" ", Arrays.copyOfRange(args, i + 1, length));
                add(results, key, operands);
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
        results.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
    }

    private String get(String[] args, int index) {
        if (index >= args.length) {
            return "";
        } else {
            return args[index];
        }
    }
}
