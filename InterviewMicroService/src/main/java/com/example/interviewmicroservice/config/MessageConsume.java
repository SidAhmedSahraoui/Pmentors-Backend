package com.example.interviewmicroservice.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageConsume {
    private Integer userId;
    private String email;
    private String username;
}
