package com.example.interviewmicroservice.repositories;

import com.example.interviewmicroservice.enums.DayName;
import com.example.interviewmicroservice.models.Day;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DayRepository extends JpaRepository<Day, Integer> {

    @Query("select d from Day d")
    @Override
    List<Day> findAll();

    @Query("select d from Day d where d.dayName = :d")
    Day findByDayName(@Param("d") DayName dayName);
}
