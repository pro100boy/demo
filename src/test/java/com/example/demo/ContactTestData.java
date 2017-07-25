package com.example.demo;

import com.google.common.collect.Lists;

import java.util.List;

public class ContactTestData {

    public static final String REGEX = "^[Б-Яб-яA-Za-z].*$";
    public static final String REGEXLOWERCASE = "^[А-Я].*$";

    public static final List<String> CONTENT = Lists.newArrayList(
            "{\"contacts\":[{\"id\":101,\"name\":\"утинцева Дарья\"},{\"id\":301,\"name\":\"аримов Казимир\"}]}",
            "{\"contacts\":[{\"id\":301,\"name\":\"аримов Казимир\"},{\"id\":101,\"name\":\"утинцева Дарья\"}]}"
    );
}
