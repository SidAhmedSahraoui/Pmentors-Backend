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
                                Space.INTERVIEW,
                                null));
        Category category2 =
                categoryRepository.save(
                        new Category(null,
                                "Frontend",
                                "Preparation au post de FE developer - HTML,CSS,JS ...",
                                Space.INTERVIEW,
                                null));

        Category category3 =
                categoryRepository.save(
                        new Category(null,
                                "Backend",
                                "Preparation au post de BE developer - Java,SQL...",
                                Space.INTERVIEW,
                                null));

            User savedUser1 = userRepository.save(
                    new User("admin",
                            "admin",
                            "admin",
                            "admin@admin.com",
                            passwordEncoder.encode("Admin@123"),
                            Role.ADMIN));
        User savedUser2 = userRepository.save(
                new User("provider1",
                        "provider",
                        "provider",
                        "provider1@provider.com",
                        passwordEncoder.encode("Provider@123"),
                        Role.PROVIDER));
        User savedUser3 = userRepository.save(
                new User("provider2",
                        "provider",
                        "provider",
                        "provider2@provider.com",
                        passwordEncoder.encode("Provider@123"),
                        Role.PROVIDER));
        User savedUser4 = userRepository.save(
                new User("provider3",
                        "provider",
                        "provider",
                        "provider3@provider.com",
                        passwordEncoder.encode("Provider@123"),
                        Role.PROVIDER));
        User savedUser5 = userRepository.save(
                new User("user",
                        "user",
                        "user",
                        "user@user.com",
                        passwordEncoder.encode("User@123"),
                        Role.USER));
            adminRepository.save(new Admin(null,savedUser1));
            providerRepository.save(new Provider(null,savedUser2,category1));
            providerRepository.save(new Provider(null,savedUser3,category2));
            providerRepository.save(new Provider(null,savedUser4,category3));

    }
}
