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

    private List<String> stringList;

    private Set<String> stringSet;

    private int[] intArray;

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
