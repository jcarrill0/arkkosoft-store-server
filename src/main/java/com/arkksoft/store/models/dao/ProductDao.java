package com.arkksoft.store.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.arkksoft.store.models.entity.Product;

@Repository
public interface ProductDao extends JpaRepository<Product, Long> {
    
}
