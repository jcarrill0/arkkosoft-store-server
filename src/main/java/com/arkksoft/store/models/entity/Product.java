package com.arkksoft.store.models.entity;

import java.util.Date;

import javax.persistence.*;

// import com.fasterxml.jackson.annotation.*;

import lombok.*;

@Entity
@Data
@Table(name = "product")
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
    private Long categoryId;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date createAt;

    /* @ManyToOne
    @JsonIgnore
    @JoinColumn(name="categoryId")
    private Category category; */

    @PrePersist
    public void prePersist() {
        createAt = new Date();
    }
}

