package com.example.interviewmicroservice.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddPlanningRequest {
    private String token;
    private String email;
    private ArrayList<Integer> daysAvailability;
    private ArrayList<Integer> slotsAvailability;
}