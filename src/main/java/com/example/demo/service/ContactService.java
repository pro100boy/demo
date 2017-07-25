package com.example.demo.service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ContactService {
    void createResponse(String regex, HttpServletResponse response) throws IOException;
}
