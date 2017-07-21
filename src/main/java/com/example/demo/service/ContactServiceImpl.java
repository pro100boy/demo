package com.example.demo.service;

import com.example.demo.model.Contact;
import com.example.demo.repository.ContactRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactServiceImpl implements ContactService{
    protected final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private ContactRepository repository;

    @Override
    public List<Contact> getAll() {
        log.info("getAll");
        return repository.findAll();
    }

    @Override
    public List<Contact> saveAll(Iterable<Contact> list) {
        log.info("saveAll");
        return repository.save(list);
    }
}
