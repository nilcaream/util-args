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

import java.util.List;
import java.util.Set;

public class TestObject {
    @Option(value = "string", alternative = "altString")
    private String stringField;

    @Option(value = "stringValue")
    private String stringValueField;

    @Option(alternative = "stringAlternative")
    private String stringAlternativeField;

    @Option(value = "int", alternative = "altInt")
    private int intField;

    @Option(value = "integer", alternative = "altInteger")
    private Integer integerField;

    @Option(value = "integerList", alternative = "altIntegerList")
    private List<Integer> integerListField;

    @Option(value = "stringArray", alternative = "altStringArray")
    private String[] stringArrayField;

    @Option(value = "v", alternative = "verbose")
    private boolean verbose;

    @Option(value = "q", alternative = "quick")
    private boolean quick;

    @Option(value = "n", alternative = "name")
    private String name;

    @Option("f")
    private static final String FINAL_NAME = "jack";

    private List<String> stringList;

    private Set<String> stringSet;

    private int[] intArray;

    public boolean isVerbose() {
        return verbose;
    }

    public boolean isQuick() {
        return quick;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStringField(String stringField) {
        this.stringField = stringField;
    }

    public String getStringField() {
        return stringField;
    }

    public String getStringValueField() {
        return stringValueField;
    }

    public String getStringAlternativeField() {
        return stringAlternativeField;
    }

    public int getIntField() {
        return intField;
    }

    public Integer getIntegerField() {
        return integerField;
    }

    public List<Integer> getIntegerListField() {
        return integerListField;
    }

    public String[] getStringArrayField() {
        return stringArrayField;
    }

    public List<String> getStringList() {
        return stringList;
    }

    public Set<String> getStringSet() {
        return stringSet;
    }

    public int[] getIntArray() {
        return intArray;
    }
}
