package com.example.demo.service;

import com.example.demo.model.Contact;
import com.example.demo.to.ContactTo;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ContactServiceImplTest extends AbstractServiceTest{

    @Autowired
    ContactService service;

    @Test
    public void testGetAll() throws Exception {
        List<Contact> contacts = service.getAll("^[Б-Яб-яA-Za-z].*$");
        Assert.assertEquals(contacts.size(), 23);
    }

    @Test(expected = NullPointerException.class)
    public void testGetNull() {
        service.getAll(null);
    }

}