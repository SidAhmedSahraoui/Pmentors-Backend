package com.example.authusersmicroservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "providers")
public class Provider {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID providerId;

    @OneToOne
    private User user;

    @ManyToOne
    private Category category;
}
