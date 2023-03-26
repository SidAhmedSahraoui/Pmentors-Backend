package com.example.authusersmicroservice;

import com.example.authusersmicroservice.models.*;
import com.example.authusersmicroservice.repositories.AdminRepository;
import com.example.authusersmicroservice.repositories.CategoryRepository;
import com.example.authusersmicroservice.repositories.ProviderRepository;
import com.example.authusersmicroservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableDiscoveryClient
public class AuthUsersMicroServiceApplication implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(AuthUsersMicroServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        Category category1 =
                categoryRepository.save(
                        new Category(null,
                                "Campus France",
                                "Preparation aux entretiens de démarche de programme campus france ",
                                null));
            User savedUser1 = userRepository.save(
                    new User("admin",
                            "admin",
                            "admin",
                            "admin@admin.com",
                            passwordEncoder.encode("AdminPass123"),
                            Role.ADMIN));
            adminRepository.save(new Admin(null,savedUser1));
            providerRepository.save(new Provider(null,savedUser1,category1));
    }
}
