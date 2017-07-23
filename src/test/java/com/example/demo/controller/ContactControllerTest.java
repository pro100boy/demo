package com.example.demo.controller;

import com.example.demo.service.ContactService;
import com.example.demo.to.ContactTo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

@RunWith(MockitoJUnitRunner.class)
public class ContactControllerTest {

    @Mock
    ContactService service;

    @InjectMocks
    ContactController sut;

    @Test
    public void testGet() throws Exception {
        ResponseEntity responseEntity = sut.getContacts("^[Б-Яб-яA-Za-z].*$", 1, 100);
        ContactTo contactTo = (ContactTo) responseEntity.getBody();
        Assert.assertEquals(contactTo.getContacts().size(), 0);
    }

}