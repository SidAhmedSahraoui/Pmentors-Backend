package com.example.interviewmicroservice.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {
    private String clientEmail;
    private String providerEmail;
    private String clientPhone;
    private String providerPhone;
    private LocalTime startsAt;
    private LocalDate appointmentDate;
    private LocalTime time;
    private LocalDate date;
}
