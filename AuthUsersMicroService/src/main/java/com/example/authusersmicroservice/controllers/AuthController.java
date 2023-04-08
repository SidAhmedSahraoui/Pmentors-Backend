package com.example.authusersmicroservice.controllers;

import com.example.authusersmicroservice.errors.AuthException;
import com.example.authusersmicroservice.DTOs.*;
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
    @PostMapping("/auth/register")
    public ResponseEntity<Object> register(
            @RequestBody RegisterRequest request
    ) {
        return service.register(request);
    }
    @PostMapping("/auth/login")
    public ResponseEntity<Object> authenticate(
            @RequestBody LoginRequest request
    ) {
        return service.authenticate(request);
    }

    @PostMapping("/user/reset-password")
    public ResponseEntity<Object> resetPassword(
            @RequestBody PasswordRequest request
    ) {
        return service.resetPassword(request);
    }

    @PatchMapping("/user/update/{id}")
    public ResponseEntity<Object> updateInfo(@PathVariable(value = "id") Long userId,
                                             @RequestBody UpdateInfoRequest request){
        return service.updateInfo(request, userId);
    }
    @DeleteMapping("/user/delete/{id}")
    public  ResponseEntity<Object> deleteUser(@PathVariable(value = "id") Long userId,
                                              @RequestBody DeleteUserRequest request){
        return  service.deleteUser(request, userId);
    }
    @GetMapping("/user/profile/{id}")
    public ResponseEntity<Object> getProfile(@PathVariable(value = "id") Long userId){
        return service.getProfile(userId);
    }

    @ExceptionHandler({AuthException.class})
    public ResponseEntity<String> handleAuthException(AuthException e){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }
}
