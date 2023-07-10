package com.example.authusersmicroservice.controllers;

import com.example.authusersmicroservice.DTOs.CategoryRequest;
import com.example.authusersmicroservice.DTOs.CreateProviderRequest;
import com.example.authusersmicroservice.DTOs.UpgradeProviderRequest;
import com.example.authusersmicroservice.models.Space;
import com.example.authusersmicroservice.repositories.AdminRepository;
import com.example.authusersmicroservice.repositories.CategoryRepository;
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
    @Autowired
    private final CategoryRepository categoryRepository;


    @PostMapping("/create-provider")
    public ResponseEntity<Object> addProvider(@RequestBody CreateProviderRequest request){

        return adminService.createProvider(request);
    }

    @DeleteMapping("/delete-provider/{username}")
    public ResponseEntity<Object> removeProvider(@PathVariable String username){

        return adminService.deleteProvider(username);
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
    public  ResponseEntity<Object> editCategory(@PathVariable(value = "id")  Integer categoryId,
                                                @RequestBody CategoryRequest request){

        return adminService.editCategory(categoryId,request);
    }

    @GetMapping("/categories")
    public ResponseEntity<Object> getAllCategories(){
        return adminService.getAllCategories();
    }

    @GetMapping("/users")
    public ResponseEntity<Object> getAllUsers(){
        return adminService.getAllUsers();
    }

    @GetMapping("/providers")
    public ResponseEntity<Object> getAllProviders(){
        return adminService.getAllProviders();
    }

    @GetMapping("/admins")
    public ResponseEntity<Object> getAllAdmins(){
        return adminService.getAllAdmins();
    }

    @GetMapping("/count/{id}")
    public Integer countUsersByRoles(@PathVariable(value = "id") Integer id){
        return adminService.getNumberOfUsersByRole(id);
    }
    @GetMapping("/count/space/{id}")
    public Integer countCategoriesBySpace(@PathVariable(value = "id") Integer id){
       try{
           if(id == 1){
               return categoryRepository.countAllBySpace(Space.INTERVIEW);
           }
           if (id == 2){
               return categoryRepository.countAllBySpace(Space.CONSULTATION);
           }
              if (id == 3){
                return categoryRepository.countAllBySpace(Space.SHARING_EXPERIENCE);
              }

       } catch (Exception e){
           return 0;
       }
       return 0;

    }
}
