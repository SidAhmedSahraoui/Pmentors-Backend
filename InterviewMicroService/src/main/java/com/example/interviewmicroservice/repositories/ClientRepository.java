package com.example.interviewmicroservice.repositories;

import com.example.interviewmicroservice.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Integer> {

    @Query("select c from Client c where c.email = :email")
    Optional<Client> findClientByEmail(@Param("email") String email);
}