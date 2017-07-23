package com.example.demo.to;

import com.example.demo.model.Contact;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
@Data
@NoArgsConstructor
@Component
public class ContactTo {
    private List<Contact> contacts;
    private long totalCount;
    private int currentPage;
    private int totalPages;
}
