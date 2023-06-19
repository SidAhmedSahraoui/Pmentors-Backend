package com.example.interviewmicroservice.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentRequest {
    private String token;
    private String clientEmail;
    private String providerUsername;
    private LocalDate date;
    private Integer day;
    private Integer slot;
    private String link;
}
