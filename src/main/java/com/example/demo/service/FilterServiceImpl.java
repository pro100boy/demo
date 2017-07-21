package com.example.demo.service;

import com.example.demo.model.Contact;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
//@Scope("prototype")
public class FilterServiceImpl implements FilterService {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Getter
    @Setter
    private Pattern pattern;

    public List<Contact> doFilter(List<Contact> contacts, String regexp) {
        //contacts.removeIf(s -> Pattern.compile(reg).matcher(s.getName()).matches());
        return contacts.parallelStream()
                .filter(s -> !Pattern.compile(regexp).matcher(s.getName()).matches())
                .collect(Collectors.toList());
    }

}
