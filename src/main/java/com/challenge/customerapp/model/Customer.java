package com.challenge.customerapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Name cannot be null")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    @Column(nullable = false, length = 100)
    private String name;

    @Positive(message = "Age must be a positive number")
    @Column(nullable = false)
    private int age;

    @NotNull(message = "Date of birth cannot be null")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Date of birth must be in the format yyyy-MM-dd")
    @Column(nullable = false)
    private String dateOfBirth;

    @Size(max = 200, message = "Address cannot exceed 200 characters")
    @Column(length = 200)
    private String address;

    @Pattern(regexp = "^[MF]?$", message = "Gender must be 'M' or 'F'")
    @Column(length = 1)
    private String gender;
}
