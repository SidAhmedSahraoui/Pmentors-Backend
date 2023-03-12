package com.example.authusersmicroservice.services;

import com.example.authusersmicroservice.models.Role;
import com.example.authusersmicroservice.models.User;
import com.example.authusersmicroservice.repositories.UserRepository;
import com.example.authusersmicroservice.response.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;

    public String createProvider(RegisterRequest request){
        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.PROVIDER)
                .enabled(true)
                .build();
        try{
            userRepository.save(user);
            return "Provider Added";
        } catch (Exception e){
            return "Bad Credentials";
        }

    }
}
