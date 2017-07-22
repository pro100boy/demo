package com.example.demo.to;

import com.example.demo.model.Contact;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
public class ContactTo {
    private List<Contact> contacts;
    private long totalCount;
    private Integer currentPage;
    private Integer totalPage;
}
