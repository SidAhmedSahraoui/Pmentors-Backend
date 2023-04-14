package com.example.interviewmicroservice.repositories;

import com.example.interviewmicroservice.models.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Integer> {

    @Query("select t from TimeSlot t")
    @Override
    List<TimeSlot> findAll();
}
