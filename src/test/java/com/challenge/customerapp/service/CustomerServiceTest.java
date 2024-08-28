package com.challenge.customerapp.service;

import com.challenge.customerapp.dto.CustomerDTO;
import com.challenge.customerapp.exception.CustomerNotFoundException;
import com.challenge.customerapp.model.Customer;
import com.challenge.customerapp.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AuditService auditService;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCustomer_Success() {
        CustomerDTO customerDTO = CustomerDTO.builder()
                .name("John Doe")
                .age(30)
                .dateOfBirth("1993-01-01")
                .build();

        Customer savedCustomer = Customer.builder()
                .id(1L)
                .name("John Doe")
                .age(30)
                .dateOfBirth("1993-01-01")
                .build();

        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);

        CustomerDTO result = customerService.createCustomer(customerDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(auditService, times(1))
                .createAuditEntry(eq("CREATE"), eq(1L), anyString(), eq("SUCCESS"));
    }

    @Test
    void testUpdateCustomer_Success() {
        Customer existingCustomer = Customer.builder()
                .id(1L)
                .name("Jane Doe")
                .age(28)
                .dateOfBirth("1995-01-01")
                .build();

        CustomerDTO customerDTO = CustomerDTO.builder()
                .name("John Doe")
                .age(30)
                .dateOfBirth("1993-01-01")
                .build();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(existingCustomer);

        CustomerDTO updatedCustomerDTO = customerService.updateCustomer(1L, customerDTO);

        assertNotNull(updatedCustomerDTO);
        assertEquals("Jane Doe", updatedCustomerDTO.getName());
        verify(auditService, times(1)).createAuditEntry(eq("UPDATE"), eq(1L), anyString(), eq("SUCCESS"));
    }

    @Test
    void testUpdateCustomer_NotFound() {
        CustomerDTO customerDTO = CustomerDTO.builder()
                .name("John Doe")
                .age(30)
                .dateOfBirth("1993-01-01")
                .build();

        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () -> {
            customerService.updateCustomer(1L, customerDTO);
        });

        assertEquals("Customer with ID 1 not found", exception.getMessage());
        verify(auditService, times(1)).createAuditEntry(eq("UPDATE"), eq(1L), anyString(), eq("FAILED"));
    }

    @Test
    void testDeleteCustomer_Success() {
        when(customerRepository.existsById(1L)).thenReturn(true);

        customerService.deleteCustomer(1L);

        verify(customerRepository, times(1)).deleteById(1L);
        verify(auditService, times(1)).createAuditEntry(eq("DELETE"), eq(1L), isNull(), eq("SUCCESS"));
    }

    @Test
    void testDeleteCustomer_NotFound() {
        when(customerRepository.existsById(1L)).thenReturn(false);

        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () -> {
            customerService.deleteCustomer(1L);
        });

        assertEquals("Customer with ID 1 not found", exception.getMessage());
        verify(auditService, times(1)).createAuditEntry(eq("DELETE"), eq(1L), isNull(), eq("FAILED"));
    }

    @Test
    void testGetAllCustomers() {
        Customer customer1 = Customer.builder()
                .id(1L)
                .name("John Doe")
                .age(30)
                .dateOfBirth("1993-01-01")
                .build();

        Customer customer2 = Customer.builder()
                .id(2L)
                .name("Jane Doe")
                .age(28)
                .dateOfBirth("1995-01-01")
                .build();

        List<Customer> customers = Arrays.asList(customer1, customer2);

        when(customerRepository.findAll()).thenReturn(customers);

        List<CustomerDTO> result = customerService.getAllCustomers();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("Jane Doe", result.get(1).getName());
    }
}
