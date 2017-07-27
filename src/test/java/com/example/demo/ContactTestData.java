package com.example.demo;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ContactTestData {

    public static final String REGEX = "^[Б-Яб-яA-Za-z].*$";
    public static final String REGEXLOWERCASE = "^[А-Я].*$";

    public static final List<String> CONTENT = Collections.unmodifiableList(
            Arrays.asList(
                    "{\"contacts\":[{\"id\":101,\"name\":\"утинцева Дарья\"},{\"id\":301,\"name\":\"аримов Казимир\"}]}",
                    "{\"contacts\":[{\"id\":301,\"name\":\"аримов Казимир\"},{\"id\":101,\"name\":\"утинцева Дарья\"}]}"
            ));
}
