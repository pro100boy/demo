package com.example.demo.service;

import com.example.demo.model.Contact;
import com.example.demo.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactServiceImpl implements ContactService{
    @Autowired
    private ContactRepository repository;

    @Override
    public List<Contact> getAll() {
        return repository.findAll();
    }

    @Override
    public List<Contact> saveAll(Iterable<Contact> list) {
        return repository.save(list);
    }
}
