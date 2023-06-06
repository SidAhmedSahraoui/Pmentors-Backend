package com.example.authusersmicroservice.config;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {

    private Long userId;
    private String email;
    private String username;

}