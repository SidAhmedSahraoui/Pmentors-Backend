package com.example.authusersmicroservice;

import com.example.authusersmicroservice.models.Role;
import com.example.authusersmicroservice.models.User;
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
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(AuthUsersMicroServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
            userRepository.save(
                    new User("Admin",
                            "admin",
                            "admin",
                            "admin@admin.com",
                            passwordEncoder.encode("AdminPass123"),
                            Role.ADMIN));
    }
}
