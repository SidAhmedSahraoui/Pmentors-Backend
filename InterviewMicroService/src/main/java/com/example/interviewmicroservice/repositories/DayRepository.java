package com.example.interviewmicroservice.repositories;

import com.example.interviewmicroservice.models.Day;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DayRepository extends JpaRepository<Day, Integer> {
}
