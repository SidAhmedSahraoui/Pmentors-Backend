package com.example.authusersmicroservice.services;

import com.example.authusersmicroservice.errors.ApiError;
import com.example.authusersmicroservice.errors.ApiResponse;
import com.example.authusersmicroservice.models.*;
import com.example.authusersmicroservice.repositories.*;
import com.example.authusersmicroservice.DTOs.*;
import com.example.authusersmicroservice.security.JwtUtilities;
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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
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
    private final JwtUtilities jwtService;
    @Autowired
    private final AuthenticationManager authenticationManager;
    @Autowired
    private ProviderRepository providerRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    public ResponseEntity<Object> register(RegisterRequest request) {
        if(repository.existsByUsername(request.getUsername()) ||
                repository.existsByEmail(request.getEmail())){
            String error = "Email or Username already exists";
            ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Invalid Credentials",error);
            return new ResponseEntity<Object>(
                    apiError, new HttpHeaders(), apiError.getStatus());
        }
        if(repository.existsByPhone(request.getPhone()) &&
                !request.getPhone().isEmpty() &&
                !request.getPhone().isBlank()){
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
        var role = roleRepository.findByRoleName(RoleName.USER);
        var roles = new ArrayList<Role>();
        roles.add(role);
        var user = User.builder()
                .username(request.getUsername())
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roles)
                .locked(false)
                .enabled(false)
                .build();

        var savedUser = repository.save(user);
        var jwtToken = jwtService.generateToken(savedUser.getEmail(), Collections.singletonList(role.getRoleName()));
        saveUserToken(savedUser, jwtToken);
        return new ResponseEntity<Object>(AuthResponse.builder()
                .token(jwtToken)
                .userId(savedUser.getUserId())
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
                List<String> rolesNames = new ArrayList<>();
                user.getRoles().forEach(r-> rolesNames.add(r.getRoleName()));

                var jwtToken = jwtService.generateToken(user.getUsername(),rolesNames);
                revokeAllUserTokens(user);
                saveUserToken(user, jwtToken);
                return new ResponseEntity<>(AuthResponse.builder()
                        .token(jwtToken)
                        .userId(user.getUserId())
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

    public ResponseEntity<Object> resetPassword(PasswordRequest request) {
        if(!repository.existsByUsername(request.getId())){
            return new ResponseEntity<Object>(new ApiResponse(HttpStatus.NOT_FOUND, "User not found"), new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
        User user = repository.findByUsername(request.getId()).get();
        if(passwordEncoder.matches(request.getOldPassword(), user.getPassword())){
            if(Pattern
                    .compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$")
                    .matcher(request.getNewPassword())
                    .find()){
                user.setPassword(passwordEncoder.encode(request.getNewPassword()));
                repository.save(user);
                return new ResponseEntity<Object>(new ApiResponse(HttpStatus.OK, "Password updated"), new HttpHeaders(), HttpStatus.OK);

            } else {
                return new ResponseEntity<Object>(new ApiResponse(HttpStatus.BAD_REQUEST, "Password should contain at least one uppercase letter, one lowercase letter, one digit and one special character."), new HttpHeaders(), HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<Object>(new ApiResponse(HttpStatus.BAD_REQUEST, "Password not correct"), new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
    }
    public ResponseEntity<Object> updateInfo(UpdateInfoRequest request, Long userId) {
        ArrayList<String> errors = new ArrayList<>();
        if(repository.existsById(userId)){
            User user = repository.findById(userId).get();
            if(!request.getUsername().equals(user.getUsername())
                    && !request.getUsername().isEmpty()
                    && !request.getUsername().isBlank()){
                if(!repository.existsByUsername(request.getUsername())){
                    user.setUsername(request.getUsername());
                } else {
                    errors.add("Username already taken");
                }
            }
            if(!request.getEmail().equals(user.getEmail())
                && !request.getEmail().isBlank()
                && !request.getEmail().isEmpty()){
                if(!repository.existsByEmail(request.getEmail())){
                    if(Pattern
                            .compile("^(.+)@(\\S+)$")
                            .matcher(request.getEmail())
                            .find()){
                        user.setEmail(request.getEmail());
                    } else {
                        errors.add("Email not valid");
                    }
                } else {
                    errors.add("Email already taken");
                }

            }
            if(!request.getPhone().equals(user.getPhone())
                    && !request.getPhone().isEmpty()
                    && !request.getPhone().isBlank()){
                if(!repository.existsByPhone(request.getPhone())){
                    user.setPhone(request.getPhone());
                } else {
                    errors.add("Phone already taken");
                }
            }

            user.setFirstName(request.getFirstname());
            user.setLastName(request.getLastname());

            if(!errors.isEmpty()){
                ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Invalid Credentials",errors);
                return new ResponseEntity<Object>(
                        apiError, new HttpHeaders(), apiError.getStatus());
            } else {
                try {
                    repository.save(user);
                    return new ResponseEntity<Object>(new ApiResponse(HttpStatus.OK, "User information saved"), new HttpHeaders(), HttpStatus.OK);

                } catch (Exception e) {
                    System.out.println(e);
                    return new ResponseEntity<Object>(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error"), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }


        } else {
            return new ResponseEntity<Object>(new ApiResponse(HttpStatus.NOT_FOUND, "User not found"), new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
    }
    public ResponseEntity<Object> deleteUser(DeleteUserRequest request, Long userId) {
        if(repository.existsById(userId)) {
            User user = repository.findById(userId).get();
            if(passwordEncoder.matches(request.getPassword(), user.getPassword())){
                if(providerRepository.existsByUser(user)){
                    providerRepository.deleteByUser(user);
                }
                try {
                    tokenRepository.deleteTokensByUser(user);
                    repository.deleteUserByUsername(user.getUsername());
                    return new ResponseEntity<Object>(new ApiResponse(HttpStatus.OK, "User account deleted"), new HttpHeaders(), HttpStatus.OK);

                } catch (Exception e) {
                    System.out.println(e);
                    return new ResponseEntity<Object>(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error"), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            return new ResponseEntity<Object>(new ApiResponse(HttpStatus.BAD_REQUEST, "Password not correct"), new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Object>(new ApiResponse(HttpStatus.NOT_FOUND, "User not found"), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Object> getProfile(Long userId) {
        if(repository.existsById(userId)) {
            try {
                User user = repository.findUserByUserId(userId);
                return new ResponseEntity<Object>( user, new HttpHeaders(), HttpStatus.OK);

            } catch (Exception e) {
                System.out.println(e);
                return new ResponseEntity<Object>(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error"), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<Object>(new ApiResponse(HttpStatus.NOT_FOUND, "User not found"), new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> getCategoriesBySpaceName(Integer spaceId) {
        if(spaceId == 0) {
            try {
                List<Category> categories = categoryRepository.findAllBySpace(Space.INTERVIEW);
                return new ResponseEntity<Object>( categories, new HttpHeaders(), HttpStatus.OK);

            } catch (Exception e) {
                System.out.println(e);
                return new ResponseEntity<Object>(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error"), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else if (spaceId == 1) {
            try {
                List<Category> categories = categoryRepository.findAllBySpace(Space.CONSULTATION);
                return new ResponseEntity<Object>( categories, new HttpHeaders(), HttpStatus.OK);

            } catch (Exception e) {
                System.out.println(e);
                return new ResponseEntity<Object>(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error"), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else if (spaceId == 2) {
            try {
                List<Category> categories = categoryRepository.findAllBySpace(Space.SHARING_EXPERIENCE);
                return new ResponseEntity<Object>( categories, new HttpHeaders(), HttpStatus.OK);

            } catch (Exception e) {
                System.out.println(e);
                return new ResponseEntity<Object>(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error"), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<Object>(new ApiResponse(HttpStatus.NOT_FOUND, "Categories not found"), new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> getCategoriesByType(String type){
        try {
            List<Category> categories = categoryRepository.findAllByType(type);
            return new ResponseEntity<Object>( categories, new HttpHeaders(), HttpStatus.OK);

        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<Object>(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error"), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> getProvidersByCategory(Long categoryId) {
        if(categoryRepository.existsById(categoryId)) {
            try {
                Collection<Provider> providers = categoryRepository.findById(categoryId).get().getProviders();
                return new ResponseEntity<Object>( providers, new HttpHeaders(), HttpStatus.OK);

            } catch (Exception e) {
                System.out.println(e);
                return new ResponseEntity<Object>(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error"), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<Object>(new ApiResponse(HttpStatus.NOT_FOUND, "Category not found"), new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> getCategoryById(Long categoryId) {
        if(categoryRepository.existsById(categoryId)) {
            try {
                var category = categoryRepository.findById(categoryId).get();
                return new ResponseEntity<Object>( category, new HttpHeaders(), HttpStatus.OK);

            } catch (Exception e) {
                System.out.println(e);
                return new ResponseEntity<Object>(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error"), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<Object>(new ApiResponse(HttpStatus.NOT_FOUND, "Category not found"), new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
    }

    public ProxyUserResponse getUserForProxy(String email) {
        try{
            User user = repository.findByEmail(email).get();
                var response = ProxyUserResponse.builder()
                        .email(user.getEmail())
                        .username(user.getUsername())
                        .build();
                return response;

        } catch (Exception e){
            return null;
        }
    }
    public ResponseEntity<Object> loadUser(GetUserRequest request) {
        try{
            User user = repository.findById(request.getUserId()).get();
            Token tokenObj = tokenRepository.findByToken(request.getToken()).get();
            if (tokenRepository.findAllValidTokenByUser(user.getUserId()).contains(tokenObj)){
                return new ResponseEntity<Object>( user, new HttpHeaders(), HttpStatus.OK);
            } else {
                return new ResponseEntity<Object>(new ApiResponse(HttpStatus.UNAUTHORIZED, "Unauthorized"), new HttpHeaders(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e){
            return new ResponseEntity<Object>(new ApiResponse(HttpStatus.NOT_FOUND, "User not found"), new HttpHeaders(), HttpStatus.NOT_FOUND);
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