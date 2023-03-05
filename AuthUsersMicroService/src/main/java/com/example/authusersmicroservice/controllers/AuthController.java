package com.example.authusersmicroservice.controllers;

import com.example.authusersmicroservice.models.UserRegister;
import com.example.authusersmicroservice.services.RegistrationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class AuthController {
    private RegistrationService registrationService;

    @PostMapping("/")
    public String register(@RequestBody UserRegister userRegister){
        return "registrationService.register(userRegister);";
    }
}
