package com.example.demo.service;

import com.example.demo.model.Contact;
import lombok.NonNull;

import java.util.List;

public interface ContactService {
    List<Contact> getAll(@NonNull String reg);

    List<Contact> saveAll(Iterable<Contact> list);

    List<Contact> getAllStandard(@NonNull String regex);
}
