package com.example.authusersmicroservice.repositories;

import com.example.authusersmicroservice.models.Category;
import com.example.authusersmicroservice.models.Space;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("select (count(c) > 0) from Category c where c.title = :title")
    boolean existsByTitle(@Param("title") String title);

    @Query("select c from Category c where c.title = :title")
    Category findByTitle(@Param("title") String title);

    @Query("select c from Category c where c.space = ?1")
    List<Category> findAllBySpace(@Param("space") Space space);

    @Query("select c from Category c where c.type = ?1")
    List<Category> findAllByType(@Param("type") String type);

}
