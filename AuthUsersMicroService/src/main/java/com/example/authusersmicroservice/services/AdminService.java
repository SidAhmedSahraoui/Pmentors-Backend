package com.example.authusersmicroservice.services;

import com.example.authusersmicroservice.exceptions.ApiError;
import com.example.authusersmicroservice.exceptions.ApiResponse;
import com.example.authusersmicroservice.models.Role;
import com.example.authusersmicroservice.models.User;
import com.example.authusersmicroservice.repositories.UserRepository;
import com.example.authusersmicroservice.response.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<Object> createProvider(RegisterRequest request) {

        boolean existUser = userRepository.existsByUsername(request.getUsername()) ||
                userRepository.existsByEmail(request.getEmail());
        if(existUser){
            String error = "Email or Username already exists";
            ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Invalid Credentials",error);
            return new ResponseEntity<Object>(
                    apiError, new HttpHeaders(), apiError.getStatus());
        }
        if(request.getPassword().length() < 8){
            String error = "Password too short";
            ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Password too short",error);
            return new ResponseEntity<Object>(
                    apiError, new HttpHeaders(), apiError.getStatus());
        }
        var user = User.builder()
                .username(request.getUsername())
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.PROVIDER)
                .build();
        userRepository.save(user);
        return new ResponseEntity<Object>(new ApiResponse(HttpStatus.OK, "Provider added"), new HttpHeaders(), HttpStatus.OK);
    }
}
