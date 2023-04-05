package com.example.authusersmicroservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "providers")
public class Provider {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long providerId;

    @OneToOne
    private User user;

    @ManyToOne
    private Category category;
}
