package com.example.demo.service;

import com.example.demo.model.Contact;
import com.example.demo.repository.ContactRepository;
import com.example.demo.utils.ContactToJsonStringConverter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.Getter;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Pattern;

@Service
@Scope("prototype")
public class ContactServiceImpl implements ContactService {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ContactRepository repository;

    private Pattern pattern;

    private final GenericConversionService conversionService = new GenericConversionService();
    private final Converter<Contact, String> converter = new ContactToJsonStringConverter();
    private final ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @Getter // getter and non-final for tests only
    private long aLong = 0;
    private boolean isFirst = true;
    // Queue for writing filtered rows (some threads) and reading (one thread)
    private final BlockingQueue<Contact> queue = new LinkedBlockingQueue<>();

    private static final int nThreads = 5;//Runtime.getRuntime().availableProcessors();

    @PostConstruct
    private void registerConverter() {
        conversionService.addConverter(converter);
    }

    @Override
    public void createResponse(@NonNull String regex, @NonNull HttpServletResponse response) throws IOException {
        aLong = 0;
        isFirst = true;
        // may throw java.util.regex.PatternSyntaxException. Handle by RestResponseEntityExceptionHandler
        this.pattern = Pattern.compile(regex);

        log.info("Start writing records filtered by regex: " + regex);

        try (PrintWriter responseWriter = response.getWriter();
        ) {
            responseWriter.print("{\"contacts\":[");
            try {
                getData(responseWriter);
            } catch (JsonProcessingException e) {
                log.info("ERROR during writing response");
            } finally {
                responseWriter.print("]}");
            }
        }
        //responseWriter.print(ow.writeValueAsString(new Contact(345l, "dsgds dsfhdfh")));
        //responseWriter.print(conversionService.convert(new Contact(343l, "dsgds dsf dsg hdfh"), String.class));
        log.info(String.format("Selecting and sorting are completed. Returned %d objects", aLong));
    }

    /**
     * generate threads (some producers and one consumer)
     *
     * @param responseWriter
     */
    private void getData(PrintWriter responseWriter) throws JsonProcessingException {
// TODO добавить пагинацию, см. Producer.run()
        List<Contact> contactPageList = repository.findAll();

        for (Contact contact : contactPageList) {
            if (!pattern.matcher(contact.getName()).matches()) {
                if (isFirst) {
                    responseWriter.write(ow.writeValueAsString(contact));
                    isFirst = false;
                } else
                    responseWriter.write("," + ow.writeValueAsString(contact));
                aLong++;
            }
        }

        // total rows into DB
        //long recTotal = repository.count();

        // number of rows for each SELECT
        //long partSize = recTotal % nThreads == 0 ? recTotal / nThreads : recTotal / nThreads + 1;

        //executor.submit(new Consumer(generator, latchConsumer));
    }

    /**
     * class for SELECT and filter portion of data
     */
    class Producer implements Runnable {
        private final CountDownLatch latch;
        private final int i;
        private final int nThreads;
        private final long partSize;

        Producer(CountDownLatch latch, int i, int nThreads, long partSize) {
            this.latch = latch;
            this.i = i;
            this.nThreads = nThreads;
            this.partSize = partSize;
        }

        @Override
        public void run() {
            // select portion of the data
            log.info(String.format("Start selecting portion %d of %d", i + 1, nThreads));
            try {
                Pageable pageable = new PageRequest(i, (int) partSize);
                // select portion of data
                List<Contact> contactPageList = repository.findAll(pageable).getContent();
                // processing
                doFilter(contactPageList);
            } finally {
                latch.countDown();
                log.info(String.format("Finished selecting and sorting portion %d of %d", i + 1, nThreads));
            }
        }

        /**
         * verify each object from list for pattern matching<br/>
         * write filtered object to shared BlockingQueue<Contact>
         *
         * @param contactPageList
         */
        private void doFilter(List<Contact> contactPageList) {
            contactPageList.stream()
                    .filter(s -> !pattern.matcher(s.getName()).matches())
                    .forEach(contact -> {
                        try {
                            queue.put(contact);
                            //aLong.incrementAndGet();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    });
        }
    }

    /**
     * Read object from shared BlockingQueue<Contact> <br/>
     * and write it to response by JsonGenerator
     */
    class Consumer implements Runnable {
        private final JsonGenerator jsonGenerator;
        private final CountDownLatch latchConsumer;

        public Consumer(JsonGenerator jsonGenerator, CountDownLatch latchConsumer) {
            this.jsonGenerator = jsonGenerator;
            this.latchConsumer = latchConsumer;
        }

        @Override
        public void run() {
            try {
                Contact contact;
                //consuming contacts until exit message is received
                while (!Objects.equals((contact = queue.take()).getName(), "}}")) {
                    jsonGenerator.writeObject(contact);
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            } finally {
                latchConsumer.countDown();
            }
        }
    }
}
