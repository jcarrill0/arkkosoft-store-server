package com.arkksoft.store.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arkksoft.store.dto.CategoryDTO;
import com.arkksoft.store.models.entity.Category;
import com.arkksoft.store.services.CategoryService;

@RestController
@RequestMapping("/api/v1")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping("/categories")
    public ResponseEntity<?> addCategory(@RequestBody CategoryDTO categoryDTO) throws Exception {
        return ResponseEntity.ok(categoryService.addCategory(categoryDTO));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping("/categories")
    public ResponseEntity<?> getAllCategories() throws Exception {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping("/categories/{id}")
    public ResponseEntity<?> getCategory(@PathVariable("id") Long id) throws Exception {
        return ResponseEntity.ok(categoryService.getCategory(id));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping("/categories/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") Long id) throws Exception {
        return ResponseEntity.ok(categoryService.deleteCategory(id));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PutMapping("/categories")
    public ResponseEntity<?> updateCategory(@RequestBody CategoryDTO category) throws Exception {
        return ResponseEntity.ok(categoryService.updateCategory(category));
    }
}
