package com.example.authusersmicroservice.services;

import com.example.authusersmicroservice.errors.ApiError;
import com.example.authusersmicroservice.errors.ApiResponse;
import com.example.authusersmicroservice.models.*;
import com.example.authusersmicroservice.repositories.AdminRepository;
import com.example.authusersmicroservice.repositories.CategoryRepository;
import com.example.authusersmicroservice.repositories.ProviderRepository;
import com.example.authusersmicroservice.repositories.UserRepository;
import com.example.authusersmicroservice.DTOs.CategoryRequest;
import com.example.authusersmicroservice.DTOs.CreateProviderRequest;
import com.example.authusersmicroservice.DTOs.UpgradeProviderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.regex.Pattern;

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
    @Autowired
    private final AdminRepository adminRepository;

    public ResponseEntity<Object> createProvider(CreateProviderRequest request) {

        ArrayList<String> errors = new ArrayList<>();

        boolean existUser = userRepository.existsByUsername(request.getUsername()) ||
                userRepository.existsByEmail(request.getEmail());

        if(existUser){
            errors.add("Username or Email already taken");
        }

        if(userRepository.existsByPhone(request.getPhone()) &&
                !request.getPhone().isEmpty() &&
                !request.getPhone().isBlank()) {
            errors.add("Phone number already exists");
        }

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

        if(!categoryRepository.existsByTitle(request.getCategoryTitle())){
            errors.add("Category not found");
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
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.PROVIDER)
                .build();
        try{
            userRepository.save(user);
        } catch (Exception e){
            System.out.println(e);
            return new ResponseEntity<Object>(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error"), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);

        }

        Category category = categoryRepository.findByTitle(request.getCategoryTitle());

        var provider = Provider.builder()
                        .user(user)
                        .category(category)
                                .build();
        try {
            providerRepository.save(provider);
        }catch (Exception e){
            System.out.println(e);
            return new ResponseEntity<Object>(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error"), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Object>(new ApiResponse(HttpStatus.OK, "Provider account added"), new HttpHeaders(), HttpStatus.OK);
    }
    public ResponseEntity<Object> deleteProvider(Long providerId) {
        if (!providerRepository.existsById(providerId)){
            String error = "Provider not found";
            ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Invalid Credentials",error);
            return new ResponseEntity<Object>(
                    apiError, new HttpHeaders(), apiError.getStatus());
        }

        try{
            Provider provider = providerRepository.findById(providerId).get();
            User user = provider.getUser();
            providerRepository.deleteByUser(user);
            user.setRole(Role.USER);
            userRepository.save(user);
            return new ResponseEntity<Object>(new ApiResponse(HttpStatus.OK, "Provider removed"), new HttpHeaders(), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<Object>(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error"), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    public  ResponseEntity<Object> upgradeProvider(UpgradeProviderRequest request) {
        if(!categoryRepository.existsByTitle(request.getCategoryTitle())){
            return new ResponseEntity<Object>(new ApiResponse(HttpStatus.BAD_REQUEST, "Category not found"), new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
        if(userRepository.existsByUsername(request.getUsername())){
           User user = userRepository.findByUsername(request.getUsername()).get();
           if(user.getRole().equals(Role.PROVIDER) || providerRepository.existsByUser(user)){
               return new ResponseEntity<Object>(new ApiResponse(HttpStatus.BAD_REQUEST, "Already provider"), new HttpHeaders(), HttpStatus.BAD_REQUEST);
           }
           user.setRole(Role.PROVIDER);
           Category category = categoryRepository.findByTitle(request.getCategoryTitle());
           try{
               userRepository.save(user);
               providerRepository.save(new Provider(null,user,category));
               return new ResponseEntity<Object>(new ApiResponse(HttpStatus.OK, "User account upgraded to provider"), new HttpHeaders(), HttpStatus.OK);
           }catch (Exception e){
               System.out.println(e);
               return new ResponseEntity<Object>(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error"), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
           }
        }

        return new ResponseEntity<Object>(new ApiResponse(HttpStatus.NOT_FOUND, "User not found"), new HttpHeaders(), HttpStatus.NOT_FOUND);

    }
    public ResponseEntity<Object> addCategory(CategoryRequest request){
        if(categoryRepository.existsByTitle(request.getTitle())){
            return new ResponseEntity<Object>(new ApiResponse(HttpStatus.BAD_REQUEST, "Category title already exists"), new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
        if(request.getTitle().isEmpty() ||
                request.getTitle().isBlank() ||
                request.getDescription().isBlank() ||
                request.getDescription().isEmpty()){
            return new ResponseEntity<Object>(new ApiResponse(HttpStatus.BAD_REQUEST, "Title or description is empty"), new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
        var category = Category.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .build();

        if(request.getSpace() == 0){
            category.setSpace(Space.INTERVIEW);
        } else if (request.getSpace() == 1) {
            category.setSpace(Space.CONSULTATION);
        } else if (request.getSpace() == 2) {
            category.setSpace(Space.SHARING_EXPERIENCE);
        } else {
            return new ResponseEntity<Object>(new ApiResponse(HttpStatus.BAD_REQUEST, "Space not found"), new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }

        try {
            categoryRepository.save(category);
            return new ResponseEntity<Object>(new ApiResponse(HttpStatus.OK, "Category added"), new HttpHeaders(), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<Object>(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error"), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public ResponseEntity<Object> editCategory(Long categoryId,CategoryRequest request){
        if(!categoryRepository.existsById(categoryId)){
            return new ResponseEntity<Object>(new ApiResponse(HttpStatus.NOT_FOUND, "Category not found"), new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
            Category category = new Category();
        try {
            category = categoryRepository.findById(categoryId).get();
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<Object>(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error"), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
            category.setDescription(request.getDescription());
        if(!request.getTitle().equals(category.getTitle()) && categoryRepository.existsByTitle(request.getTitle())){
            return new ResponseEntity<Object>(new ApiResponse(HttpStatus.BAD_REQUEST, "Category title already taken"), new HttpHeaders(), HttpStatus.BAD_REQUEST);

        }
        category.setTitle(request.getTitle());
        if(request.getSpace() == 0){
            category.setSpace(Space.INTERVIEW);
        } else if (request.getSpace() == 1) {
            category.setSpace(Space.CONSULTATION);
        } else if (request.getSpace() == 2) {
            category.setSpace(Space.SHARING_EXPERIENCE);
        } else {
            return new ResponseEntity<Object>(new ApiResponse(HttpStatus.BAD_REQUEST, "Space not found"), new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
        try {
            categoryRepository.save(category);
            return new ResponseEntity<Object>(new ApiResponse(HttpStatus.OK, "Category details updated"), new HttpHeaders(), HttpStatus.OK);
        } catch (Exception e){
            System.out.println(e);
            return new ResponseEntity<Object>(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error"), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity<Object> getAllCategories(){
        try {
            return new ResponseEntity<Object>( categoryRepository.findAll(Sort.by("Space")), new HttpHeaders(), HttpStatus.OK);
        }catch (Exception e){
            System.out.println(e);
            return new ResponseEntity<Object>(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error"), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity<Object> getAllUsers(){
        try {
            return new ResponseEntity<Object>( userRepository.findAll(), new HttpHeaders(), HttpStatus.OK);
        }catch (Exception e){
            System.out.println(e);
            return new ResponseEntity<Object>(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error"), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity<Object> getAllUsersByRole(Integer role){
        if (role == 0 || role == 1 || role == 2){
            try {
                return new ResponseEntity<Object>( userRepository.findUsersByRole(role == 0 ? Role.ADMIN : (role == 1 ? Role.PROVIDER : Role.USER)), new HttpHeaders(), HttpStatus.OK);
            }catch (Exception e){
                System.out.println(e);
                return new ResponseEntity<Object>(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error"), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<Object>(new ApiResponse(HttpStatus.NOT_ACCEPTABLE, "Role not found"), new HttpHeaders(), HttpStatus.NOT_ACCEPTABLE);
    }

    public ResponseEntity<Object> getAllAdmins(){
            try {
                return new ResponseEntity<Object>( adminRepository.findAll(), new HttpHeaders(), HttpStatus.OK);
            }catch (Exception e){
                System.out.println(e);
                return new ResponseEntity<Object>(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error"), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

    public ResponseEntity<Object> getAllProviders(){
        try {
            return new ResponseEntity<Object>( providerRepository.findAll(), new HttpHeaders(), HttpStatus.OK);
        }catch (Exception e){
            System.out.println(e);
            return new ResponseEntity<Object>(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error"), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
