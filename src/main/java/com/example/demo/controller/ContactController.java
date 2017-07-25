package com.example.demo.controller;

import com.example.demo.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ContactController {
    static final String REST_URL = "/hello";

    @Autowired
    private ContactService service;

    @GetMapping(REST_URL)
    public void getContacts(@RequestParam(value = "nameFilter", defaultValue = "^[В-Яв-яC-Zc-z].*$") String regex,
                            HttpServletResponse response) throws IOException {

        response.setCharacterEncoding("UTF8");
        response.setContentType("application/json");

        service.createResponse(regex, response);
    }
}
