package com.arkksoft.store.models.entity;

import java.util.*;

import javax.persistence.*;

// import com.fasterxml.jackson.annotation.*;  

import lombok.*;

@Entity
@Data
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String description;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date createAt;

    /* @JsonManagedReference
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Product> products; */
    
    @PrePersist
    public void prePersist() {
        createAt = new Date();
    }
}
