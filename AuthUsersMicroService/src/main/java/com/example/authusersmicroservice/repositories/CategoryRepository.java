package com.example.authusersmicroservice.repositories;

import com.example.authusersmicroservice.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    boolean existsByTitle(String title);

    Category findByTitle(String title);

}
