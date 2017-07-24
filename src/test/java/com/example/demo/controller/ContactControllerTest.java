package com.example.demo.controller;

import com.example.demo.service.ContactService;
import com.example.demo.to.ContactTo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.json.JacksonTester;

import static com.example.demo.ContactTestData.REGEX;

@RunWith(MockitoJUnitRunner.class)
public class ContactControllerTest {

    @Mock
    ContactService service;

    @InjectMocks
    ContactController sut;

    private JacksonTester<ContactTo> json;

    @Before
    public void setup() {
        ObjectMapper objectMapper = new ObjectMapper();
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    public void testGet() throws Exception {
        ContactTo contactTo = sut.getContacts(REGEX, 1, 100);
        Assert.assertEquals(contactTo.getContacts().size(), 0);
    }
}