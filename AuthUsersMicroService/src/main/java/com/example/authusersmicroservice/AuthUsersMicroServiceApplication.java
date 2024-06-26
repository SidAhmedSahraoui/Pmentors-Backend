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
                                "Entretien Campus France",
                                "Pratiquez l'entretien Campus France avec nos experts n'hésitez plus ! réservez maintenant un entretien 1: 1, " +
                                        "vous pouvez également réserver un entretien avec un expert pour vous aider à préparer votre dossier de candidature.",
                                CategoryType.STUDY,
                                Space.INTERVIEW,
                                null));
        Category category2 =
                categoryRepository.save(
                        new Category(null,
                                "Frontend Developer Interview",
                                "Assurez-vous d'avoir les connaissances nécessaires pour obtenir " +
                                        "votre premier emploi en tant qu'ingénieur frontend et grandir dans " +
                                        "votre première étape dans le domaine informatique",
                                CategoryType.HIRING,
                                Space.INTERVIEW,

                                null));

        Category category3 =
                categoryRepository.save(
                        new Category(null,
                                "Backend Developer Interview",
                                "Pratiquer toutes les questions de compétences backend," +
                                        "de la conception de l'API à la mise en œuvre et aux meilleures pratiques, Java, SQL ...",
                                CategoryType.HIRING,
                                Space.INTERVIEW,
                                null));

        Category category4 =
                categoryRepository.save(
                        new Category(null,
                                "Study in Canada",
                                "Choisissez le pays et l'université : Renseignez-vous sur les différents pays et les universités qui offrent les programmes d'études qui vous intéressent. Considérez des facteurs tels que la réputation académique, les frais de scolarité, etc.",
                                CategoryType.STUDY,
                                Space.INTERVIEW,
                                null));
        Category category5 =
                categoryRepository.save(
                        new Category(null,
                                 "Study in Turkey",
                                "Préparez les documents nécessaires : Rassemblez les documents requis pour votre candidature, tels que les relevés de notes, les lettres de recommandation, les essais personnels, etc. Vérifiez les exigences spécifiques de chaque université.",
                                CategoryType.STUDY,
                                Space.INTERVIEW,
                                null));
        Category category6 =
                categoryRepository.save(
                        new Category(null,
                                "Ecole supérieure de l'hôtellerie et la restauration",
                                "Préparez les documents nécessaires : Rassemblez les documents requis pour votre candidature, tels que les relevés de notes, les lettres de recommandation, les essais personnels, etc. Vérifiez les exigences spécifiques de chaque université.",
                                CategoryType.STUDY,
                                Space.INTERVIEW,
                                null));
        Category category7 =
                categoryRepository.save(
                        new Category(null,
                                "Ecole supérieure de tourisme",
                                "Préparez les documents nécessaires : Rassemblez les documents requis pour votre candidature, tels que les relevés de notes, les lettres de recommandation, les essais personnels, etc. Vérifiez les exigences spécifiques de chaque université.",
                                CategoryType.STUDY,
                                Space.INTERVIEW,
                                null));
        Category category8 =
                categoryRepository.save(
                        new Category(null,
                                "Part time designer job",
                                "Préparez les documents nécessaires : Rassemblez les documents requis pour votre candidature, tels que les relevés de notes, les lettres de recommandation, les essais personnels, etc. Vérifiez les exigences spécifiques de chaque université.",
                                CategoryType.PART_TIME,
                                Space.INTERVIEW,
                                null));
        Category category9 =
                categoryRepository.save(
                        new Category(null,
                                "Part time video editor job",
                                "Préparez les documents nécessaires : Rassemblez les documents requis pour votre candidature, tels que les relevés de notes, les lettres de recommandation, les essais personnels, etc. Vérifiez les exigences spécifiques de chaque université.",
                                CategoryType.STUDY,
                                Space.INTERVIEW,
                                null));
        Category category10 =
                categoryRepository.save(
                        new Category(null,
                                "Internship in Canada",
                                "Préparez les documents nécessaires : Rassemblez les documents requis pour votre candidature, tels que les relevés de notes, les lettres de recommandation, les essais personnels, etc. Vérifiez les exigences spécifiques de chaque université.",
                                CategoryType.TRAINING,
                                Space.INTERVIEW,
                                null));
            User savedAdmin = userRepository.save(
                    new User("admin",
                            "admin",
                            "admin",
                            "admin@pmentors.dz",
                            passwordEncoder.encode("Admin@123"),
                            new ArrayList<>()));
        User savedProvider1 = userRepository.save(
                new User("provider1",
                        "ahmed",
                        "ahmed",
                        "provider1@pmentors.dz",
                        passwordEncoder.encode("Provider@123"),
                        new ArrayList<>()));
        User savedProvider2 = userRepository.save(
                new User("provider2",
                        "ali",
                        "ali",
                        "provider2@pmentors.dz",
                        passwordEncoder.encode("Provider@123"),
                        new ArrayList<>()));

        User savedProvider3 = userRepository.save(
                new User("provider3",
                        "MOHAMED",
                        "mohamed",
                        "provider3@pmentors.dz",
                        passwordEncoder.encode("Provider@123"),
                        new ArrayList<>()));

        User savedUser1 = userRepository.save(
                new User("user1",
                        "user",
                        "user",
                        "user1@pmentors.dz",
                        passwordEncoder.encode("User@123"),
                        new ArrayList<>()));
        User savedUser2 = userRepository.save(
                new User("user2",
                        "user",
                        "user",
                        "user2@pmentors.dz",
                        passwordEncoder.encode("User@123"),
                        new ArrayList<>()));

        User savedUser3 = userRepository.save(
                new User("user3",
                        "user",
                        "user",
                        "user3@pmentors.dz",
                        passwordEncoder.encode("User@123"),
                        new ArrayList<>()));
        // admin
        savedAdmin.getRoles().add(userRole);
        savedAdmin.getRoles().add(adminRole);

        // provider 1
        savedProvider1.getRoles().add(userRole);
        savedProvider1.getRoles().add(providerRole);

        // provider 2
        savedProvider2.getRoles().add(userRole);
        savedProvider2.getRoles().add(providerRole);

        // provider 3
        savedProvider3.getRoles().add(userRole);
        savedProvider3.getRoles().add(providerRole);

        // users
        savedUser1.getRoles().add(userRole);
        savedUser2.getRoles().add(userRole);
        savedUser3.getRoles().add(userRole);

        userRepository.save(savedAdmin);
        userRepository.save(savedProvider1);
        userRepository.save(savedProvider2);
        userRepository.save(savedProvider3);
        userRepository.save(savedUser1);
        userRepository.save(savedUser2);
        userRepository.save(savedUser3);

            adminRepository.save(new Admin(null,savedAdmin));
            providerRepository.save(new Provider(null,savedProvider1,category1));
            providerRepository.save(new Provider(null,savedProvider2,category2));
            providerRepository.save(new Provider(null,savedProvider3,category3));


    }
}
