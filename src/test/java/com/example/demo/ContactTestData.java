package com.example.demo;

import com.google.common.collect.Lists;

import java.util.List;

public class ContactTestData {

    public static final String REGEX = "^[Б-Яб-яA-Za-z].*$";
    public static final String REGEXLOWERCASE = "^[А-Я].*$";

    public static final List<String> CONTENT = Lists.newArrayList(
            "{\"contacts\":[{\"id\":101,\"name\":\"утинцева Дарья\"},{\"id\":301,\"name\":\"аримов Казимир\"}],\"totalCount\":2,\"currentPage\":1,\"totalPages\":1}",
            "{\"contacts\":[{\"id\":301,\"name\":\"аримов Казимир\"},{\"id\":101,\"name\":\"утинцева Дарья\"}],\"totalCount\":2,\"currentPage\":1,\"totalPages\":1}"
    );

    public static final List<Long> EXPECTEDLISTIDS = Lists.newArrayList(
            25L, 26L, 41L, 59L,
            116L, 135L, 184L, 188L, 190L, 199L,
            225L, 256L, 259L, 266L,
            301L, 325L, 356L, 359L, 366L,
            442L, 446L, 466L);
}
