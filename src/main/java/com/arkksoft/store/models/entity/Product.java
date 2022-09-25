package com.arkksoft.store.models.entity;

import java.util.Date;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.*;

import lombok.*;

@Entity
@Data
@Table(name = "product")
/* @JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "id") */
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String brand;
    private Integer model;
    private Float price;
    private String description;
    private String image;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date createAt;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name="categoryId")
    private Category category;

    @PrePersist
    public void prePersist() {
        createAt = new Date();
    }
}

