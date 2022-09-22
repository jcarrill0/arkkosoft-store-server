package com.arkksoft.store.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.arkksoft.store.models.entity.Category;

@Repository
public interface CategoryDao extends JpaRepository<Category, Long> {
    
}
