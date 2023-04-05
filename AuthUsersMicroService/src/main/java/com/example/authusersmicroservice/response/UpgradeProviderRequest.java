package com.example.authusersmicroservice.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpgradeProviderRequest {

    private String username;
    private String categoryTitle;

}
