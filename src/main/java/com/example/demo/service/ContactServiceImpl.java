package com.example.demo.service;

import com.example.demo.model.Contact;
import com.example.demo.repository.ContactRepository;
import com.example.demo.utils.ContactToJsonStringConverter;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
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

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JsonFactory factory = new JsonFactory();
    @Getter // getter and non-final for tests only
    private AtomicLong aLong = new AtomicLong(0);

    // Queue for writing filtered rows (some threads) and reading (one thread)
    private final BlockingQueue<Contact> queue = new LinkedBlockingQueue<>();

    private static final int nThreads = 5;//Runtime.getRuntime().availableProcessors();

    @PostConstruct
    private void registerConverter()
    {
        conversionService.addConverter(converter);
    }

    @Override
    public void createResponse(@NonNull String regex, @NonNull HttpServletResponse response) throws IOException {
        aLong.set(0);

        // may throw java.util.regex.PatternSyntaxException. Handle by RestResponseEntityExceptionHandler
        this.pattern = Pattern.compile(regex);

        log.info("Start writing records filtered by regex: " + regex);

        try (PrintWriter responseWriter = response.getWriter();
             JsonGenerator generator = factory.createGenerator(responseWriter)
        ) {
            generator.setCodec(objectMapper);

            // generate and write json data to response
            generator.writeStartObject();
            generator.writeFieldName("contacts");
            generator.writeStartArray();
            getDataParallel(generator);
            generator.flush();
            generator.writeEndArray();
            generator.writeEndObject();
        }

        log.info(String.format("Selecting and sorting are completed. Returned %d objects", aLong.get()));
    }

    /**
     * generate threads (some producers and one consumer)
     *
     * @param generator
     */
    private void getDataParallel(JsonGenerator generator) {

        ExecutorService executor = Executors.newFixedThreadPool(nThreads + 1);
        CountDownLatch latchProducers = new CountDownLatch(nThreads);
        CountDownLatch latchConsumer = new CountDownLatch(1);

        // total rows into DB
        long recTotal = repository.count();

        // number of rows for each SELECT
        long partSize = recTotal % nThreads == 0 ? recTotal / nThreads : recTotal / nThreads + 1;

        // run multi thread selecting
        for (int i = 0; i < nThreads; i++) {
            executor.submit(new Producer(latchProducers, i, nThreads, partSize));
        }
        executor.submit(new Consumer(generator, latchConsumer));

        try {
            // wait until all producers are finishing
            latchProducers.await();

            // put object-marker to queue
            queue.put(new Contact(-1L, "}}"));
            // wait until consumer are finishing
            latchConsumer.await();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }

    /**
     * class for SELECT and filter portion of data
     */
    class Producer implements Runnable {
        private final CountDownLatch latch;
        private final int i;
        private final int nThreads;
        private final long partSize;

        public Producer(CountDownLatch latch, int i, int nThreads, long partSize) {
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
                            aLong.incrementAndGet();
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
                while ((contact = queue.take()).getName() != "}}") {
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
