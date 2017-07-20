package com.example.demo.service;

import com.example.demo.model.Contact;

import java.util.List;

public interface ContactService {
    List<Contact> getAll();

    List<Contact> saveAll(Iterable<Contact> list);
}
