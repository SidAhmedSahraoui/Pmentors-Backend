package com.example.interviewmicroservice.repositories;

import com.example.interviewmicroservice.models.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long> {

        @Query("select (count(p) > 0) from Provider p where p.email = :email")
        Boolean existsByEmail(@Param("email") String email);

        @Query("select p from Provider p where p.email = :email")
        Optional<Provider> findByEmail(@Param("email") String email);


        @Query("select p from Provider p where p.username = ?1")
        Optional<Provider> findByUsername(@Param("username") String username);

}
