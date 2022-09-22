package com.arkksoft.store.models.entity;

import java.util.Date;

import javax.persistence.*;

import lombok.*;

@Entity
@Data
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Float price;
    private String description;
    private String image;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date createAt;

    @ManyToOne
    @JoinColumn(name="categoryId")
    private Category category;
}

