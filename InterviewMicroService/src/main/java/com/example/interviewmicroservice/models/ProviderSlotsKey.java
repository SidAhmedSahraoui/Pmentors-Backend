package com.example.interviewmicroservice.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
class ProviderSlotsKey implements Serializable {

    @Column(name = "provider_id")
    private Long providerId;

    @Column(name = "time_slot_id")
    private Integer timeSlotId;

}

