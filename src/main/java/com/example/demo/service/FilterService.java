package com.example.demo.service;

import com.example.demo.model.Contact;

import java.util.List;

public interface FilterService {
    List<Contact> doFilter(List<Contact> contacts, String regexp);
}
