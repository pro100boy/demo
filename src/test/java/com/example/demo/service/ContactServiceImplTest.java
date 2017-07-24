package com.example.demo.service;

import com.example.demo.model.Contact;
import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.example.demo.ContactTestData.EXPECTEDLISTIDS;
import static com.example.demo.ContactTestData.REGEX;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ContactServiceImplTest extends AbstractServiceTest{

    @Autowired
    ContactService service;

    @Test
    public void testGetAll() throws Exception {
        List<Contact> contacts = service.getAll(REGEX);
        assertThat(contacts.size(), is(22));

        List<Long> actualIDs = contacts.stream()
                .map(contact -> contact.getId())
                .sorted()
                .collect(collectingAndThen(toList(), ImmutableList::copyOf));

        List<String> actualNames = contacts.stream()
                .map(contact -> contact.getName())
                .collect(collectingAndThen(toList(), ImmutableList::copyOf));

        assertThat(actualIDs, equalTo(EXPECTEDLISTIDS));
        assertThat(actualNames, hasItem(startsWith("А")));
    }

    @Test(expected = NullPointerException.class)
    public void testGetNull() {
        service.getAll(null);
    }

}