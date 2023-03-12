package com.example.authusersmicroservice.controllers;

import com.example.authusersmicroservice.response.RegisterRequest;
import com.example.authusersmicroservice.services.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    @Autowired
    private final AdminService adminService;

    @PostMapping
    public String addProvider(@RequestBody RegisterRequest request){
        return adminService.createProvider(request);
    }
}
