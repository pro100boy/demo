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

import java.util.Collections;
import java.util.Comparator;
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

    // TODO проверять на отсутствие nameFilter, чтоб не регексить, а сразу возвращать весь список
    // TODO использовать ResponseEntity с кодами ошибок (статусами)
    @GetMapping(REST_URL)
    public ContactTo getContacts(
            @RequestParam(value = "nameFilter", defaultValue = "^[В-Яв-я].*$") String regex,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "cnt", required = false, defaultValue = "100") int pageSize) {

        if (pageSize <= 0 || page <= 0) {
            throw new IllegalArgumentException("invalid page size: " + pageSize);
        }

        if (!regexInit.equals(regex)) {
            contacts = service.getAll(regex);
            regexInit = regex;

/*
            // if needed
            // sorting by Name
            Collections.sort(contacts, (o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
            // sorting by id
            Collections.sort(contacts, Comparator.comparing(Contact::getId));
*/
        }
        return getPage(contacts, page, pageSize);
    }

    @GetMapping("/standard")
    public List<Contact> getAllStandard(@RequestParam(value = "nameFilter", defaultValue = "") String regex) {
        List<Contact> contacts = service.getAllStandard(regex);
        Collections.sort(contacts, Comparator.comparingLong(o -> o.getId()));
        return contacts;
    }
}
