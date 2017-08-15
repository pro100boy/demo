package com.example.demo.service;

import com.example.demo.repository.ContactRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Service
@Scope("prototype")
public class ContactServiceImpl implements ContactService {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ContactRepository repository;

    /**
     * Method writes all contacts which names DOES NOT MATCH with regex
     *
     * @param regex regular expression for filter.
     * @param response response to write in
     */
    @Override
    public void createResponse(@NonNull String regex, @NonNull HttpServletResponse response) throws IOException {
        log.info("Start writing records filtered by regex: " + regex);

        try (PrintWriter responseWriter = response.getWriter()
        ) {
            responseWriter.print("{\"contacts\":[");
            try {
                long rows = repository.printAll(regex, responseWriter);
                log.info(String.format("The selection is complete. %d items were returned", rows));
            } catch (JsonProcessingException e) {
                log.info("ERROR during writing response");
            } finally {
                responseWriter.print("]}");
            }
        }
    }
}
