package com.example.authusersmicroservice.controllers;

import com.example.authusersmicroservice.exceptions.AuthException;
import com.example.authusersmicroservice.response.*;
import com.example.authusersmicroservice.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class AuthController {

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

    @PostMapping("/reset-password")
    public ResponseEntity<Object> resetPassword(
            @RequestBody PasswordRequest request
    ) {
        return service.resetPassword(request);
    }

    @PatchMapping("/update")
    public ResponseEntity<Object> updateInfo(@RequestBody UpdateInfoRequest request){
        return service.updateInfo(request);
    }
    @DeleteMapping("/delete")
    public  ResponseEntity<Object> deleteUser(@RequestBody DeleteUserRequest request){
        return  service.deleteUser(request);
    }

    @ExceptionHandler({AuthException.class})
    public ResponseEntity<String> handleAuthException(AuthException e){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }
}
