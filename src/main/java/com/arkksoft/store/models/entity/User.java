package com.arkksoft.store.models.entity;

import java.util.Date;

import javax.persistence.*;

import lombok.*;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date createAt; 

    @ManyToOne
    @JoinColumn(name="roleId")
    private Role role;
}

