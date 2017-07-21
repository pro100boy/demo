package com.example.demo.repository;

import com.example.demo.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    @Override
    default List<Contact> findAll() {
        return null;
    }
}
