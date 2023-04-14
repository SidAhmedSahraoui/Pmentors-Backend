package com.example.authusersmicroservice.controllers;

import com.example.authusersmicroservice.errors.AuthException;
import com.example.authusersmicroservice.DTOs.*;
import com.example.authusersmicroservice.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private final AuthService service;

    @PostMapping("/register")
    public ResponseEntity<Object> register(
            @RequestBody RegisterRequest request
    ) {
        return service.register(request);
    }
    @PostMapping("/login")
    public ResponseEntity<Object> authenticate(
            @RequestBody LoginRequest request
    ) {
        return service.authenticate(request);
    }

    @GetMapping("/proxy/provider/{id}/{token}")
    public ProxyUserResponse  proxyProvider(
            @PathVariable("id") String email,
            @PathVariable("token") String token){
        return service.getProviderForProxy(email, token);
    }

    @ExceptionHandler({AuthException.class})
    public ResponseEntity<String> handleAuthException(AuthException e){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }
}
