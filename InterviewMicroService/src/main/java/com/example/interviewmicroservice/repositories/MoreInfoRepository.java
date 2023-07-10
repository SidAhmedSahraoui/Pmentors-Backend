package com.example.interviewmicroservice.repositories;

import com.example.interviewmicroservice.models.MoreInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MoreInfoRepository extends JpaRepository<MoreInfo, Integer> {

    @Query("select m from MoreInfo m where m.appointmentId = ?1")
    Optional<MoreInfo> findByAppointmentId(Integer appointmentId);
}
