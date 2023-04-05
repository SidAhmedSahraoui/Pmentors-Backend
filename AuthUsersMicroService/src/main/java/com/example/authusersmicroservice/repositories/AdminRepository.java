package com.example.authusersmicroservice.repositories;

import com.example.authusersmicroservice.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AdminRepository extends JpaRepository<Admin, Long> {
}
