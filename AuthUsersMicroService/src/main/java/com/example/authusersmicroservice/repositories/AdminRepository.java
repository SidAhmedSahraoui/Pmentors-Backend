package com.example.authusersmicroservice.repositories;

import com.example.authusersmicroservice.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AdminRepository extends JpaRepository<Admin, Integer> {

    @Query("select count(*) from Admin")
    Integer countAdmins();
}
