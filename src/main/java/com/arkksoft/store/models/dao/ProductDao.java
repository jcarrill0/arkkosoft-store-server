package com.arkksoft.store.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.arkksoft.store.models.entity.Product;

@Repository
public interface ProductDao extends JpaRepository<Product, Long> {
    @Query(value = "select * from product order by id", nativeQuery = true)
    List<Product> getAllProducts();
}
