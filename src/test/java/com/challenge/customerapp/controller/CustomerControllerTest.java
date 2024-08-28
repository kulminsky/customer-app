package com.challenge.customerapp.controller;

import com.challenge.customerapp.dto.CustomerDTO;
import com.challenge.customerapp.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    @Test
    void testGetAllCustomers() throws Exception {
        CustomerDTO customer1 = CustomerDTO.builder()
                .id(1L)
                .name("John Doe")
                .age(30)
                .dateOfBirth("1993-01-01")
                .build();

        CustomerDTO customer2 = CustomerDTO.builder()
                .id(2L)
                .name("Jane Doe")
                .age(28)
                .dateOfBirth("1995-01-01")
                .build();

        List<CustomerDTO> customers = Arrays.asList(customer1, customer2);

        when(customerService.getAllCustomers()).thenReturn(customers);

        mockMvc.perform(get("/customers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)))
                .andExpect(jsonPath("$[0].name", is("John Doe")))
                .andExpect(jsonPath("$[1].name", is("Jane Doe")));
    }

    @Test
    void testCreateCustomer() throws Exception {
        CustomerDTO customerDTO = CustomerDTO.builder()
                .name("John Doe")
                .age(30)
                .dateOfBirth("1993-01-01")
                .build();

        CustomerDTO savedCustomer = CustomerDTO.builder()
                .id(1L)
                .name("John Doe")
                .age(30)
                .dateOfBirth("1993-01-01")
                .build();

        when(customerService.createCustomer(any(CustomerDTO.class))).thenReturn(savedCustomer);

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Doe\", \"age\":30, \"dateOfBirth\":\"1993-01-01\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("John Doe")));
    }

    @Test
    void testUpdateCustomer() throws Exception {
        CustomerDTO updatedCustomer = CustomerDTO.builder()
                .id(1L)
                .name("John Doe Updated")
                .age(31)
                .dateOfBirth("1993-01-01")
                .build();

        when(customerService.updateCustomer(anyLong(), any(CustomerDTO.class))).thenReturn(updatedCustomer);

        mockMvc.perform(put("/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Doe Updated\", \"age\":31, \"dateOfBirth\":\"1993-01-01\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("John Doe Updated")));
    }

    @Test
    void testDeleteCustomer() throws Exception {
        mockMvc.perform(delete("/customers/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
