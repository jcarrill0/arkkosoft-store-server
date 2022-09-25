package com.arkksoft.store.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.arkksoft.store.dto.ProductDTO;
import com.arkksoft.store.services.ProductService;

@RestController
@RequestMapping("/api/v1")
public class ProductController {
    @Autowired
    private ProductService productService;
    
    @PostMapping("/products")
    public ResponseEntity<?> addProduct(@RequestPart("product") ProductDTO productDTO, @RequestPart("imageFile") MultipartFile file) throws Exception {
        return ResponseEntity.ok(productService.addProduct(productDTO, file));
    }

    @GetMapping("/products")
    public ResponseEntity<?> getAllProducts() throws Exception {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<?> getProduct(@PathVariable("id") Long id) throws Exception {
        return ResponseEntity.ok(productService.getProduct(id));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Long id) throws Exception {
        return ResponseEntity.ok(productService.deleteProduct(id));
    }

    @PatchMapping("/products/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable("id") Long id, @RequestPart("product") ProductDTO productDTO, @RequestPart("imageFile") MultipartFile file) throws Exception {
        return ResponseEntity.ok(productService.updateProduct(id, productDTO, file));
    }
}
