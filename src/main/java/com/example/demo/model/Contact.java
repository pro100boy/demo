package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;

/**
 * Created by Pavel Galushkin on 19.07.2017.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contacts")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(value = AccessType.PROPERTY)
    private Long id;

    @NotEmpty(message = "*Please provide name")
    @Column(name = "name", nullable = false)
    @Length(min = 3, max = 255, message = "*Name must have at least 3 characters")
    private String name;

    public Contact(String name)
    {
        this.id = null;
        this.name = name;
    }
}
