package com.example.demo.service;

import com.example.demo.model.Contact;
import com.example.demo.repository.ContactRepository;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class ContactServiceImpl implements ContactService{
    protected final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private ContactRepository repository;

    @Getter
    @Setter
    private Pattern pattern;

    private List<Contact> doFilter(List<Contact> contacts, String reg) {
        contacts.removeIf(s -> Pattern.compile(reg).matcher(s.getName()).matches());
        return contacts;
    }

    @Override
    public List<Contact> getAll(@NonNull String reg) {
        log.info(reg.isEmpty() ? "getAll" : "getAll with regex: " + reg);
        return doFilter(repository.findAll(), reg);
    }

    @Override
    public List<Contact> saveAll(Iterable<Contact> list) {
        log.info("saveAll");
        return repository.save(list);
    }
}
