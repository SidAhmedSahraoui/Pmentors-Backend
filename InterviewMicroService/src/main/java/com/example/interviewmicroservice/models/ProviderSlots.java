package com.example.interviewmicroservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "provider_slots")
class ProviderSlots {

    @EmbeddedId
    private ProviderSlotsKey providerSlotsKey;

    @ManyToOne
    @MapsId("timeSlotId")
    @JoinColumn(name = "time_slot_id")
    private TimeSlot  timeSlot;

    @ManyToOne
    @MapsId("providerId")
    @JoinColumn(name = "provider_id")
    private Provider provider;

}