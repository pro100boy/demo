package com.example.demo.controller;

import com.example.demo.model.Contact;
import com.example.demo.service.ContactService;
import com.example.demo.to.ContactTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.example.demo.utils.ServiceUtil.getPage;

/**
 * Created by User on 19.07.2017.
 */
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ContactController {
    static final String REST_URL = "/hello";

    @Autowired
    private ContactService service;

    private String regexInit = "";
    private List<Contact> contacts;

    @GetMapping(REST_URL)
    public ContactTo getContacts(
            @RequestParam(value = "nameFilter", defaultValue = "^[В-Яв-яC-Zc-z].*$") String regex,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "cnt", required = false, defaultValue = "100") int pageSize) {

        if (pageSize <= 0 || page <= 0) {
            throw new IllegalArgumentException();
        }

        // To avoid to reselect list from DB
        if (!regexInit.equals(regex)) {
            contacts = service.getAll(regex);
            regexInit = regex;
        }

        return getPage(contacts, page, pageSize);
    }
}
