package com.challenge.customerapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Action cannot be null")
    @Column(nullable = false)
    private String action;

    private Long customerId;

    private String request;

    @NotNull(message = "Status cannot be null")
    @Column(nullable = false)
    private String status;

    @NotNull(message = "Creation datetime cannot be null")
    @Column(nullable = false)
    private LocalDateTime creationDatetime;
}
