package com.nilcaream.utilargs;

import com.nilcaream.utilargs.model.Option;

import java.util.List;

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
}
