package com.example.authusersmicroservice.DTOs;
import lombok.AllArgsConstructor;
        import lombok.Builder;
        import lombok.Data;
        import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProxyUserRequest {
    private String token;
    private String email;
}