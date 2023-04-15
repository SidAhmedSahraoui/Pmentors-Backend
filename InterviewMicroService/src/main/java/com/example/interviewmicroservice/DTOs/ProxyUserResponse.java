package com.example.interviewmicroservice.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProxyUserResponse {
    private Long userId;
    private String email;
    private String username;
}