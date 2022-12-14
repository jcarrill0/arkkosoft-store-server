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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.arkksoft.store.dto.ProductDTO;
import com.arkksoft.store.services.ProductService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@SecurityRequirement(name = "Authentication")
@RequestMapping("/api/v1")
public class ProductController {
    @Autowired
    private ProductService productService;
    
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping(value = "/products", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> addProduct(@RequestPart("imageFile") MultipartFile file, @RequestPart("product") ProductDTO productDTO) throws Exception {
        return ResponseEntity.ok(productService.addProduct(productDTO, file));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping("/products")
    public ResponseEntity<?> getAllProducts() throws Exception {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping("/products/{id}")
    public ResponseEntity<?> getProduct(@PathVariable("id") Long id) throws Exception {
        return ResponseEntity.ok(productService.getProduct(id));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Long id) throws Exception {
        return ResponseEntity.ok(productService.deleteProduct(id));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PutMapping(value = "/products", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> updateProduct(@RequestPart("product") ProductDTO productDTO, @RequestPart("imageFile") MultipartFile file) throws Exception {
        return ResponseEntity.ok(productService.updateProduct(productDTO, file));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PutMapping("/products/not_img")
    public ResponseEntity<?> updateProductNotImage(@RequestBody ProductDTO productDTO) throws Exception {
        return ResponseEntity.ok(productService.updateProductNotImage(productDTO));
    }
}
