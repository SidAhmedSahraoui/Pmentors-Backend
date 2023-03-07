package com.example.authusersmicroservice.controllers;

import com.example.authusersmicroservice.models.UserRegisterRequest;
import com.example.authusersmicroservice.services.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/auth")
@AllArgsConstructor
public class AuthController {

    private final RegistrationService registrationService;

    @PostMapping("/register")
    public String register(@RequestBody UserRegisterRequest request) {
        return registrationService.register(request);
    }

    @GetMapping(path = "/confirm")
    public String confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }

}
