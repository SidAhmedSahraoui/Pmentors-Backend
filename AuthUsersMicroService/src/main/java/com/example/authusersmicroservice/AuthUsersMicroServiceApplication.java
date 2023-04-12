package com.example.authusersmicroservice;

import com.example.authusersmicroservice.models.*;
import com.example.authusersmicroservice.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

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

    @Autowired
    private RoleRepository roleRepository;

    public static void main(String[] args) {
        SpringApplication.run(AuthUsersMicroServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        // default roles
        Role adminRole = roleRepository.save(new Role(RoleName.ADMIN));
        Role userRole = roleRepository.save(new Role(RoleName.USER));
        Role providerRole = roleRepository.save(new Role(RoleName.PROVIDER));


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

            User savedAdmin = userRepository.save(
                    new User("admin",
                            "admin",
                            "admin",
                            "admin@admin.com",
                            passwordEncoder.encode("Admin@123"),
                            new ArrayList<>()));
        User savedProvider1 = userRepository.save(
                new User("provider1",
                        "provider",
                        "provider",
                        "provider1@provider.com",
                        passwordEncoder.encode("Provider@123"),
                        new ArrayList<>()));
        User savedProvider2 = userRepository.save(
                new User("provider2",
                        "provider",
                        "provider",
                        "provider2@provider.com",
                        passwordEncoder.encode("Provider@123"),
                        new ArrayList<>()));

        User savedUser1 = userRepository.save(
                new User("user1",
                        "user",
                        "user",
                        "user1@user.com",
                        passwordEncoder.encode("User@123"),
                        new ArrayList<>()));
        User savedUser2 = userRepository.save(
                new User("user2",
                        "user",
                        "user",
                        "user2@user.com",
                        passwordEncoder.encode("User@123"),
                        new ArrayList<>()));

        User savedUser3 = userRepository.save(
                new User("user3",
                        "user",
                        "user",
                        "user3@user.com",
                        passwordEncoder.encode("User@123"),
                        new ArrayList<>()));
        // admin
        savedAdmin.getRoles().add(userRole);
        savedAdmin.getRoles().add(providerRole);
        savedAdmin.getRoles().add(adminRole);

        // provider 1
        savedProvider1.getRoles().add(userRole);
        savedProvider1.getRoles().add(providerRole);

        // provider 2
        savedProvider2.getRoles().add(userRole);
        savedProvider2.getRoles().add(providerRole);

        // users
        savedUser1.getRoles().add(userRole);
        savedUser2.getRoles().add(userRole);
        savedUser3.getRoles().add(userRole);

        userRepository.save(savedAdmin);
        userRepository.save(savedProvider1);
        userRepository.save(savedProvider2);
        userRepository.save(savedUser1);
        userRepository.save(savedUser2);
        userRepository.save(savedUser3);

            adminRepository.save(new Admin(null,savedAdmin));
            providerRepository.save(new Provider(null,savedAdmin,category1));
            providerRepository.save(new Provider(null,savedProvider1,category2));
            providerRepository.save(new Provider(null,savedProvider2,category3));

    }
}
