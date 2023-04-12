package com.example.interviewmicroservice.repositories;

import com.example.interviewmicroservice.models.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Integer> {
}
