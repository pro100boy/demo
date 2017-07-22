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

/**
 * Created by User on 19.07.2017.
 */
@RestController
@RequestMapping(value = ContactController.REST_URL, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ContactController {
    static final String REST_URL = "/hello";

    @Autowired
    private ContactService service;

    @Autowired
    private ContactTo contactTo;

    private String regexInit = "";
    private List<Contact> contacts;

    @GetMapping
    public List<Contact> getAll(@RequestParam(value = "nameFilter", defaultValue = "") String regex) {
        List<Contact> contacts = service.getAll(regex);
        Collections.sort(contacts, Comparator.comparingLong(o -> o.getId()));
        return contacts;
    }

    @GetMapping("/pages")
    public ContactTo getPage(
            @RequestParam(value = "nameFilter", defaultValue = "") String regex,
            @RequestParam("page") int page,
            @RequestParam(value = "cnt", required = false, defaultValue = "500_000") int pageSize) {

        if (!regexInit.equals(regex)) {
            contacts = service.getAll(regex);
            regexInit = regex;
            Collections.sort(contacts, Comparator.comparingLong(o -> o.getId()));
        }
        return getPage(contacts, page, pageSize);
    }

    @GetMapping("/standard")
    public List<Contact> getAllStandard(@RequestParam(value = "nameFilter", defaultValue = "") String regex) {
        List<Contact> contacts = service.getAllStandard(regex);
        Collections.sort(contacts, Comparator.comparingLong(o -> o.getId()));
        return contacts;
    }

    /*
     * returns a view (not a new list) of the sourceList for the
     * range based on page and pageSize
     * @param sourceList
     * @param page
     * @param pageSize
     * @return
     */
    public ContactTo getPage(List<Contact> sourceList, int page, int pageSize) {
        if (pageSize <= 0 || page <= 0) {
            throw new IllegalArgumentException("invalid page size: " + pageSize);
        }

        int fromIndex = (page - 1) * pageSize;
        if (sourceList == null || sourceList.size() < fromIndex) {
            return contactTo;
        }

        // toIndex exclusive
        contactTo.setContacts(sourceList.subList(fromIndex, Math.min(fromIndex + pageSize, sourceList.size())));
        contactTo.setCurrentPage(page);
        contactTo.setTotalCount(sourceList.size());
        contactTo.setTotalPages((sourceList.size()/pageSize)+1);
        return contactTo;
    }
}
