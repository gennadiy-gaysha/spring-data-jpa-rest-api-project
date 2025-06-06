package com.gaysha.spring_data_jpa_setup.domains;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// - getters and setters for all fields
// - toString()
// - equals() and hashCode()
// - makes the fields private if they aren't already
@Data
@NoArgsConstructor
@AllArgsConstructor
// Enables the Builder Pattern, which lets you create objects in a flexible, readable way:
// Book book = Book.builder().title("1984").isbn("1234567890").authorId(1L).build();
@Builder
// JPA level - marks a Java class as a persistent entity that will be mapped to a
// database table:
@Entity
@Table(name = "authors")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "author_id_seq")
    /*// To customize the default settings:
    @SequenceGenerator(
            // Match with generator name in @GeneratedValue - Java-side name of the generator:
            name = "author_id_seq",
            // Database-side name of the sequence - the actual sequence name in DB:
            sequenceName = "author_id_seq",
            // how many IDs to allocate at once (1 is typical)
            allocationSize = 2
    )*/
    private Long id;
    private String name;
    private Integer age;
}
