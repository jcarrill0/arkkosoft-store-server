package com.arkksoft.store.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arkksoft.store.dto.CategoryDTO;
import com.arkksoft.store.services.CategoryService;

@RestController
@RequestMapping("/api/v1")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    
    @PostMapping("/categories")
    public ResponseEntity<?> addCategory(@RequestBody CategoryDTO categoryDTO) throws Exception {
        return ResponseEntity.ok(categoryService.addCategory(categoryDTO));
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getAllCategories() throws Exception {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<?> getCategory(@PathVariable("id") Long id) throws Exception {
        return ResponseEntity.ok(categoryService.getCategory(id));
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") Long id) throws Exception {
        return ResponseEntity.ok(categoryService.deleteCategory(id));
    }

    @PatchMapping("/categories/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable("id") Long id, @RequestBody CategoryDTO categoryDTO) throws Exception {
        return ResponseEntity.ok(categoryService.updateCategory(id, categoryDTO));
    }
}
