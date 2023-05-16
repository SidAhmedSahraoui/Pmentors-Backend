package com.example.authusersmicroservice.controllers;

import com.example.authusersmicroservice.DTOs.DeleteUserRequest;
import com.example.authusersmicroservice.DTOs.PasswordRequest;
import com.example.authusersmicroservice.DTOs.UpdateInfoRequest;
import com.example.authusersmicroservice.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final AuthService service;

    @PostMapping("/reset-password")
    public ResponseEntity<Object> resetPassword(
            @RequestBody PasswordRequest request
    ) {
        return service.resetPassword(request);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<Object> updateInfo(@PathVariable(value = "id") Long userId,
                                             @RequestBody UpdateInfoRequest request){
        return service.updateInfo(request, userId);
    }
    @DeleteMapping("/delete/{id}")
    public  ResponseEntity<Object> deleteUser(@PathVariable(value = "id") Long userId,
                                              @RequestBody DeleteUserRequest request){
        return  service.deleteUser(request, userId);
    }
    @GetMapping("/profile/{id}")
    public ResponseEntity<Object> getProfile(@PathVariable(value = "id") Long userId){
        return service.getProfile(userId);
    }

    @GetMapping("/space/{id}/categories")
    public ResponseEntity<Object> getCategoriesBySpaceName(@PathVariable(value = "id") Integer spaceId){
        return service.getCategoriesBySpaceName(spaceId);
    }

    @GetMapping("/category/{id}/providers")
    public ResponseEntity<Object> getProvidersByCategory(@PathVariable(value = "id") Long categoryId){
        return service.getProvidersByCategory(categoryId);
    }
    @GetMapping("/category/{id}/")
    public ResponseEntity<Object> getCategoryById(@PathVariable(value = "id") Long categoryId){
        return service.getCategoryById(categoryId);
    }
}
