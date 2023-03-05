package com.example.authusersmicroservice.services;

import com.example.authusersmicroservice.models.UserRegister;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegistrationService {
    public String register(UserRegister userRegister){
        return "Okay";
    }
}
