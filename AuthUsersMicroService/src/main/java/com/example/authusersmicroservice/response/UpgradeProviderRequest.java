package com.example.authusersmicroservice.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpgradeProviderRequest {

    private UUID userId;
    private String categoryTitle;

}