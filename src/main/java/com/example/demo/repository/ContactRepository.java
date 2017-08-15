package com.example.demo.repository;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.PrintWriter;

public interface ContactRepository {
    Long printAll(String regex, PrintWriter responseWriter) throws JsonProcessingException;
}
