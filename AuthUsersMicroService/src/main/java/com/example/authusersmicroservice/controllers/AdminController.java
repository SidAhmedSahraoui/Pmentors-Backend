package com.example.authusersmicroservice.controllers;

import com.example.authusersmicroservice.response.CategoryRequest;
import com.example.authusersmicroservice.response.CreateProviderRequest;
import com.example.authusersmicroservice.response.UpgradeProviderRequest;
import com.example.authusersmicroservice.services.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    @Autowired
    private final AdminService adminService;

    @PostMapping("/create-provider")
    public ResponseEntity<Object> addProvider(@RequestBody CreateProviderRequest request){

        return adminService.createProvider(request);
    }

    @DeleteMapping("/delete-provider/{providerId}")
    public ResponseEntity<Object> removeProvider(@PathVariable Long providerId){

        return adminService.deleteProvider(providerId);
    }

    @PatchMapping("/upgrade-provider")
    public  ResponseEntity<Object> upgradeProvider(@RequestBody UpgradeProviderRequest request){

        return adminService.upgradeProvider(request);
    }

    @PostMapping("/add-category")
    public  ResponseEntity<Object> createCategory(@RequestBody CategoryRequest request){

        return adminService.addCategory(request);
    }

    @PatchMapping("/edit-category/{id}")
    public  ResponseEntity<Object> editCategory(@PathVariable(value = "id")  Long categoryId,
                                                @RequestBody CategoryRequest request){

        return adminService.editCategory(categoryId,request);
    }

    @GetMapping("/categories")
    public ResponseEntity<Object> getAllCategories(){
        return adminService.getAllCategories();
    }
}
