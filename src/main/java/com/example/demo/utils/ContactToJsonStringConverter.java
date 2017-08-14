package com.example.demo.utils;

import com.example.demo.model.Contact;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;

public class ContactToJsonStringConverter implements Converter<Contact, String> {
    @Override
    public String convert(Contact contact) {

        if (contact == null){
            throw new ConversionFailedException(TypeDescriptor.valueOf(Contact.class),
                    TypeDescriptor.valueOf(String.class), contact, null);
        }

        //{"id":53,"name":"Зуб Алексей"}
        String contactStr = "{\"id\":" +
                contact.getId() +
                ",\"name\":\"" +
                contact.getName() +
                "\"}";
        return contactStr;
    }
}
