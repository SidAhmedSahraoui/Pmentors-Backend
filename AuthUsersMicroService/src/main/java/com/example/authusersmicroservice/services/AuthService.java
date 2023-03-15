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
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        }catch (BadCredentialsException e){
            System.out.println(e);
        }

      if(repository.existsByUsername(request.getEmail()) ||
          repository.existsByEmail(request.getEmail())){
          var user = repository.findUserByEmailOrUsername(request.getEmail())
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


