package com.example.authusersmicroservice.repositories;

import com.example.authusersmicroservice.models.Category;
import com.example.authusersmicroservice.models.Space;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByTitle(@Param("title") String title);

    Category findByTitle(@Param("title") String title);

    @Query("select c from Category c where c.space = :space")
    List<Category> findCategoriesBySpace(@Param("space") Space space);

}
