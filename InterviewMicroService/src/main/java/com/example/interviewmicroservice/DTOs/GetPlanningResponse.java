package com.example.interviewmicroservice.DTOs;


import com.example.interviewmicroservice.models.Day;
import com.example.interviewmicroservice.models.TimeSlot;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetPlanningResponse {
    private Set<Day> days;
    private Set<TimeSlot> slots;
}
