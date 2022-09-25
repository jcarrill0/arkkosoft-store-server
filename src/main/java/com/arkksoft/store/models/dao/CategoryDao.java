package com.arkksoft.store.models.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.arkksoft.store.models.entity.Category;

@Repository
public interface CategoryDao extends JpaRepository<Category, Long> {
    @Query(value = "select * from category order by id", nativeQuery = true)
    List<Category> getAllCategories();

    Optional<Category> findByName(String name);
}
