package com.example.authusersmicroservice.repositories;

import com.example.authusersmicroservice.models.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProviderRepository extends JpaRepository<Provider, UUID> {

    boolean deleteByProviderId(UUID providerId);

}
