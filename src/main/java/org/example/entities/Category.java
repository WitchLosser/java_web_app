package org.example.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="tbl_categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="name", nullable = false, length = 100)
    private String name;
    @Column(name="image", nullable = false, length = 100)
    private String image;
    @Column(name="description", nullable = false, length = 150)
    private String description;
}