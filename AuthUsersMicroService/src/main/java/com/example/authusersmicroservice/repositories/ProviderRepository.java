package com.example.authusersmicroservice.repositories;

import com.example.authusersmicroservice.models.Provider;
import com.example.authusersmicroservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ProviderRepository extends JpaRepository<Provider, Long> {
    @Query("select (count(p) > 0) from Provider p where p.user = :user")
    boolean existsByUser(@Param("user") User user);
    @Transactional
    @Modifying
    @Query("delete from Provider p where p.user = :user")
    void deleteByUser(@Param("user") User user);
}
