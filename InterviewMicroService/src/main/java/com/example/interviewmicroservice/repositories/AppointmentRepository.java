package com.example.interviewmicroservice.repositories;

import com.example.interviewmicroservice.models.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("select a from Appointment a where a.providerEmail = :email")
    List<Appointment> findAppointmentsByProviderEmail(@Param("email") String email);

    @Query("select a from Appointment a where a.clientEmail = :email")
    List<Appointment> findAppointmentsByClientEmail(@Param("email") String email);

    @Query("""
            select (count(a) > 0) from Appointment a
            where a.appointmentDate = :d and a.clientEmail = :c and a.providerEmail = :p and a.startsAt = :s and a.endsAt = :e""")
    boolean existsByAppointmentDateAndClientEmailAndProviderEmailAndStartsAtAndEndsAt(@Param("d") LocalDate date,
                                                                                      @Param("c") String client,
                                                                                      @Param("p") String provider,
                                                                                      @Param("s") LocalTime startsAt,
                                                                                      @Param("e") LocalTime endsAt);
}