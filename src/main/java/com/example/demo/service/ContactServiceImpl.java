package com.example.demo.service;

import com.example.demo.model.Contact;
import com.example.demo.repository.ContactRepository;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CountDownLatch;

@Service
public class ContactServiceImpl implements ContactService {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ContactRepository repository;

    @Autowired
    private FilterService filterService;

    @Override
    public List<Contact> getAll(@NonNull String reg) {
        log.info(reg.isEmpty() ? "getAll" : "getAll with regex: " + reg);
        // TODO repository.findAll(): реализовать вытягивание частями. Каждую часть в своем потоке фильтровать. Отфильтрованное вливать в ConcurrentLinkedQueue или сразу писать в Response
        return filterService.doFilter(repository.findAll(), reg);
    }

    @Override
    public List<Contact> saveAll(Iterable<Contact> list) {
        log.info("saveAll");
        return repository.save(list);
    }

    private void getDataParallel()
    {
        CountDownLatch latch = new CountDownLatch(4);

    }


}
