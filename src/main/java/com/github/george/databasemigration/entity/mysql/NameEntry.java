package com.github.george.databasemigration.entity.mysql;

import javax.persistence.*;

/**
 * This class represents an entity that has id and string name stored in the MySQL database.
 */
@Entity
@Table(name = "name_entry")
public class NameEntry {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
