package com.arkksoft.store.controllers;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arkksoft.store.dto.AuthDTO;
import com.arkksoft.store.services.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostConstruct
    public void initRoleAndUser() {
        authService.initUserSuperAdmin();
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody AuthDTO authDTO) throws Exception {
        return ResponseEntity.ok(authService.signin(authDTO));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody AuthDTO authDTO) throws Exception {
        return ResponseEntity.ok(authService.signup(authDTO));
    }
}
