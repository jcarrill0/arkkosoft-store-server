package com.arkksoft.store.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ProductController {
    
    @PostMapping("/products")
    public ResponseEntity<?> createCharacter(@RequestPart("character") CharacterDTO characterDTO, @RequestPart("imageFile") MultipartFile file) throws Exception {
        return ResponseEntity.ok(characterService.addCharacter(characterDTO, file));
    }
}
