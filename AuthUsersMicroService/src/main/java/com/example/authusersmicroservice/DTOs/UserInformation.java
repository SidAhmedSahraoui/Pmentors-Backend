package com.example.authusersmicroservice.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInformation {

    private Long id;
    private String username;
    private String email;
    private Boolean locked;
    private Boolean enabled;
}