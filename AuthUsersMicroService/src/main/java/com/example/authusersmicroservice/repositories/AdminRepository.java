package com.example.authusersmicroservice.repositories;

import com.example.authusersmicroservice.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AdminRepository extends JpaRepository<Admin, UUID> {
}
