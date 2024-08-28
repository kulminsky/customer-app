package com.challenge.customerapp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerDTO {

    private Long id;

    @NotNull(message = "Name cannot be null")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @Positive(message = "Age must be a positive number")
    private int age;

    @NotNull(message = "Date of birth cannot be null")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Date of birth must be in the format yyyy-MM-dd")
    private String dateOfBirth;

    @Size(max = 200, message = "Address cannot exceed 200 characters")
    private String address;

    @Pattern(regexp = "^[MF]?$", message = "Gender must be 'M' or 'F'")
    private String gender;

}
