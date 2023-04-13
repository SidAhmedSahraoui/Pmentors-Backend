package com.example.interviewmicroservice.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddPlanningRequest {
    private String token;
    private String email;
    private Map<Integer,Boolean> daysAvailability;
    private Map<Integer,Boolean> slotsAvailability;
}