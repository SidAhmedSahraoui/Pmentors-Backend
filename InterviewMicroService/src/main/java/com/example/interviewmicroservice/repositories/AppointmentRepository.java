package com.example.interviewmicroservice.repositories;

import com.example.interviewmicroservice.models.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

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
    @Query("select a from Appointment a where a.clientEmail = ?1")
    List<Appointment> findAllByClient(String clientEmail);

    @Query("select a from Appointment a where a.providerEmail = ?1")
    List<Appointment> findAllByProvider(String providerEmail);

    @Transactional
    @Modifying
    @Query("delete from Appointment a where a.appointmentId = :id")
    int deleteByAppointmentId(@Param("id") Integer id);
}
