package com.example.demo.controller;

import com.example.demo.model.Contact;
import com.example.demo.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * Created by User on 19.07.2017.
 */
@RestController
@RequestMapping(value = ContactController.REST_URL, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ContactController {
    static final String REST_URL = "/hello";

    @Autowired
    private ContactService service;

/*    @GetMapping(value = "/create")
    public Collection<Contact> getCreated() {
        Collection<Contact> list = Arrays.asList(
                new Contact("Pasha"),
                new Contact("Misha")
        );
        return service.saveAll(list);
    }*/

    @GetMapping
    public Collection<Contact> getAll(@RequestParam(value = "nameFilter", defaultValue = "") String reg) {
        return service.getAll(reg);
    }
}
