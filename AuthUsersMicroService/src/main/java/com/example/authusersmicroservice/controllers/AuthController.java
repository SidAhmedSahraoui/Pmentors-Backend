package com.example.authusersmicroservice.controllers;

import com.example.authusersmicroservice.exceptions.AuthException;
import com.example.authusersmicroservice.response.AuthResponse;
import com.example.authusersmicroservice.response.LoginRequest;
import com.example.authusersmicroservice.response.RegisterRequest;
import com.example.authusersmicroservice.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private final AuthService service;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @RequestBody RegisterRequest request
    ) throws AuthException {
        try{
            return ResponseEntity.ok(service.register(request));
        } catch (Exception e) {
            throw new AuthException("Bad Credentials", e);
        }
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(
            @RequestBody LoginRequest request
    ) {
        try {
            return ResponseEntity.ok(service.authenticate(request));
        }catch (Exception e){
            throw new AuthException("Yooow", e);
        }
        }
    @ExceptionHandler({AuthException.class})
    public ResponseEntity<String> handleAuthException(AuthException e){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }
}
