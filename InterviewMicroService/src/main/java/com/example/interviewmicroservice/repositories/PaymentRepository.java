package com.example.interviewmicroservice.repositories;

import com.example.interviewmicroservice.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    @Query("select p from Payment p where p.appointmentId = ?1")
    Optional<Payment> findByAppointmentId(Integer id);
}
