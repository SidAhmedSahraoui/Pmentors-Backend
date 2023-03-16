package com.example.authusersmicroservice.services;

import com.example.authusersmicroservice.exceptions.ApiError;
import com.example.authusersmicroservice.models.*;
import com.example.authusersmicroservice.repositories.TokenRepository;
import com.example.authusersmicroservice.repositories.UserRepository;
import com.example.authusersmicroservice.response.AuthResponse;
import com.example.authusersmicroservice.response.LoginRequest;
import com.example.authusersmicroservice.response.RegisterRequest;
import com.example.authusersmicroservice.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.regex.Pattern;


@Service
@RequiredArgsConstructor
public class AuthService {
    @Autowired
    private final UserRepository repository;
    @Autowired
    private final TokenRepository tokenRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<Object> register(RegisterRequest request) {
        if(repository.existsByUsername(request.getUsername()) ||
                repository.existsByEmail(request.getEmail())){
            String error = "Email or Username already exists";
            ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Invalid Credentials",error);
            return new ResponseEntity<Object>(
                    apiError, new HttpHeaders(), apiError.getStatus());
        }
        if(repository.existsByPhone(request.getPhone())){
            String error = "Phone number already exists";
            ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Invalid Credentials",error);
            return new ResponseEntity<Object>(
                    apiError, new HttpHeaders(), apiError.getStatus());
        }

        ArrayList<String> errors = new ArrayList<>();

        if(request.getPassword().length() < 8){
            errors.add("Password too short");
        }
        if(!Pattern
                .compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$")
                .matcher(request.getPassword())
                .find()){
            errors.add("Password should contain at least one uppercase letter, one lowercase letter, one digit and one special character.");
        }
        if(!Pattern
                .compile("^(.+)@(\\S+)$")
                .matcher(request.getEmail())
                .find()){
            errors.add("Email is not correct");
        }
        if(!errors.isEmpty()){
            ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Invalid Credentials",errors);
            return new ResponseEntity<Object>(
                    apiError, new HttpHeaders(), apiError.getStatus());
        }
        var user = User.builder()
                .username(request.getUsername())
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        var savedUser = repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        saveUserToken(savedUser, jwtToken);
        return new ResponseEntity<Object>(AuthResponse.builder()
                .token(jwtToken)
                .build(), new HttpHeaders(), HttpStatus.OK);
    }

    public ResponseEntity<Object> authenticate(LoginRequest request) {
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getCredential(),
                            request.getPassword()
                    )
            );
        }catch (BadCredentialsException e){
            System.out.println(e);
        }

      if(repository.existsByUsername(request.getCredential()) ||
          repository.existsByEmail(request.getCredential()) ||
            repository.existsByPhone(request.getCredential())){
          var user = repository.findUserByEmailOrUsernameOrPhone(request.getCredential())
                  .orElseThrow();
          if(passwordEncoder.matches(request.getPassword(),user.getPassword())){
              var jwtToken = jwtService.generateToken(user);
              revokeAllUserTokens(user);
              saveUserToken(user, jwtToken);
              return new ResponseEntity<>(AuthResponse.builder()
                      .token(jwtToken)
                      .build(), new HttpHeaders(), HttpStatus.OK);
          } else {
          String error = "Password Incorrect";
          ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED, "Password Incorrect",error);
          return new ResponseEntity<Object>(
                  apiError, new HttpHeaders(), apiError.getStatus());
      }
      }else {
          String error = "User not exists";
          ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Invalid Credentials",error);
          return new ResponseEntity<Object>(
                  apiError, new HttpHeaders(), apiError.getStatus());
      }
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getUserId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}


