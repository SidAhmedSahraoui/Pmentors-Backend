package com.example.authusersmicroservice.services;

import com.example.authusersmicroservice.exceptions.ApiError;
import com.example.authusersmicroservice.exceptions.ApiResponse;
import com.example.authusersmicroservice.models.Category;
import com.example.authusersmicroservice.models.Provider;
import com.example.authusersmicroservice.models.Role;
import com.example.authusersmicroservice.models.User;
import com.example.authusersmicroservice.repositories.CategoryRepository;
import com.example.authusersmicroservice.repositories.ProviderRepository;
import com.example.authusersmicroservice.repositories.UserRepository;
import com.example.authusersmicroservice.response.CategoryRequest;
import com.example.authusersmicroservice.response.CreateProviderRequest;
import com.example.authusersmicroservice.response.UpgradeProviderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final CategoryRepository categoryRepository;
    @Autowired
    private final ProviderRepository providerRepository;

    public ResponseEntity<Object> createProvider(CreateProviderRequest request) {

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
        if(!categoryRepository.existsByTitle(request.getCategoryTitle())){
            String error = "Category not found";
            ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Category not found",error);
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
        Category category = categoryRepository.findByTitle(request.getCategoryTitle());
        var provider = Provider.builder()
                        .user(user)
                        .category(category)
                                .build();
        Provider savedProvider = providerRepository.save(provider);
        //category.getProviders().add(savedProvider);
        return new ResponseEntity<Object>(new ApiResponse(HttpStatus.OK, "Provider added"), new HttpHeaders(), HttpStatus.OK);
    }
    public ResponseEntity<Object> deleteProvider(UUID providerId) {
        if (!providerRepository.existsById(providerId)){
            String error = "Provider not found";
            ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Invalid Credentials",error);
            return new ResponseEntity<Object>(
                    apiError, new HttpHeaders(), apiError.getStatus());
        }
        providerRepository.deleteById(providerId);
        return new ResponseEntity<Object>(new ApiResponse(HttpStatus.OK, "Provider removed"), new HttpHeaders(), HttpStatus.OK);

    }

    public  ResponseEntity<Object> upgradeProvider(UpgradeProviderRequest request) {

        return new ResponseEntity<Object>(new ApiResponse(HttpStatus.OK, "User upgraded to Provider"), new HttpHeaders(), HttpStatus.OK);

    }
    public ResponseEntity<Object> addCategory(CategoryRequest request){
        if(categoryRepository.existsByTitle(request.getTitle())){
            String error = "Category title already exists";
            ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Invalid Credentials",error);
            return new ResponseEntity<Object>(
                    apiError, new HttpHeaders(), apiError.getStatus());
        }
        if(request.getTitle().isEmpty() || request.getTitle().isBlank() || request.getDescription().isBlank() || request.getDescription().isEmpty()){
            String error = "Title or description is empty";
            ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Invalid Credentials",error);
            return new ResponseEntity<Object>(
                    apiError, new HttpHeaders(), apiError.getStatus());
        }
        var category = Category.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .build();
        categoryRepository.save(category);
        return new ResponseEntity<Object>(new ApiResponse(HttpStatus.OK, "Category added"), new HttpHeaders(), HttpStatus.OK);

    }

    public ResponseEntity<Object> editCategory(CategoryRequest request){

        return new ResponseEntity<Object>(new ApiResponse(HttpStatus.OK, "Category added"), new HttpHeaders(), HttpStatus.OK);

    }
}
