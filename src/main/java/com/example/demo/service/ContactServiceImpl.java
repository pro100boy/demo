package com.example.demo.service;

import com.example.demo.model.Contact;
import com.example.demo.repository.ContactRepository;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class ContactServiceImpl implements ContactService {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ContactRepository repository;

    @Autowired
    private FilterService filterService;

    @Override
    public List<Contact> getAll(@NonNull String regex) {
        log.info(regex.isEmpty() ? "getAll" : "getAll with regex: " + regex);
        // TODO repository.findAll(): реализовать вытягивание частями. Каждую часть в своем потоке фильтровать. Отфильтрованное вливать в ConcurrentLinkedQueue или сразу писать в Response
        getDataParallel(regex);
        log.info(String.format("Returned %d objects", commonContactList.size()));
        return commonContactList;
    }

    @Override
    public List<Contact> getAllStandard(@NonNull String regex) {
        log.info(regex.isEmpty() ? "getAllStandard" : "getAllStandard with regex: " + regex);
        // TODO repository.findAll(): реализовать вытягивание частями. Каждую часть в своем потоке фильтровать. Отфильтрованное вливать в ConcurrentLinkedQueue или сразу писать в Response
        List<Contact> contacts = filterService.doFilter(repository.findAll(), regex);
        log.info(String.format("Returned %d standard objects", contacts.size()));
        return contacts;
    }

    @Override
    public List<Contact> saveAll(Iterable<Contact> list) {
        log.info("saveAll");
        return repository.save(list);
    }

    private void getDataParallel(String regex) {
        int nThreads = 5;//Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(nThreads);
        CountDownLatch latch = new CountDownLatch(nThreads);
        commonContactList.clear();

        for (int i = 0; i < nThreads; i++) {
            executor.submit(new DBSelector(latch, i, nThreads, regex));
        }

        try {
            latch.await();  // wait until latch counted down to 0
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        log.info("Selecting and sorting are completed");
    }

    class DBSelector implements Runnable {
        private CountDownLatch latch;
        private int i;
        private int nThreads;
        private String regex;

        public DBSelector(CountDownLatch latch, int i, int nThreads, String regex) {
            this.latch = latch;
            this.i = i;
            this.nThreads = nThreads;
            this.regex = regex;
        }

        @Override
        public void run() {
            // select portion of the data
            log.info(String.format("Start selecting portion %d of %d", i+1, nThreads));
            try {
                Pageable pageable = createPageRequest();
                List<Contact> contactPageList = repository.findAll(pageable).getContent();
                commonContactList.addAll(filterService.doFilter(contactPageList, regex));
            } finally {
                latch.countDown();
                log.info(String.format("Finished selecting and sorting portion %d of %d", i+1, nThreads));
            }
        }

        private Pageable createPageRequest() {
            return new PageRequest(i, 100, Sort.Direction.ASC, "id");
        }
    }

    private CopyOnWriteArrayList<Contact> commonContactList = new CopyOnWriteArrayList<>();

}
