package com.challenge.customerapp.service;

import com.challenge.customerapp.exception.CustomerNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import com.challenge.customerapp.dto.CustomerDTO;
import com.challenge.customerapp.model.Customer;
import com.challenge.customerapp.repository.CustomerRepository;
import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    private final AuditService auditService;

    public CustomerDTO createCustomer(@Valid CustomerDTO customerDTO) {
        Customer customer = convertToEntity(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        auditService.createAuditEntry("CREATE", savedCustomer.getId(), customerDTO.toString(), "SUCCESS");
        return convertToDTO(savedCustomer);
    }

    public CustomerDTO updateCustomer(Long id, @Valid CustomerDTO customerDTO) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> {
                    auditService.createAuditEntry("UPDATE", id, customerDTO.toString(), "FAILED");
                    return new CustomerNotFoundException("Customer with ID " + id + " not found");
                });

        Customer updatedCustomer = Customer.builder()
                .id(customer.getId())  // retain the existing ID
                .name(customerDTO.getName())
                .age(customerDTO.getAge())
                .dateOfBirth(customerDTO.getDateOfBirth())
                .address(customerDTO.getAddress())
                .gender(customerDTO.getGender())
                .build();

        updatedCustomer = customerRepository.save(updatedCustomer);

        auditService.createAuditEntry("UPDATE", updatedCustomer.getId(), customerDTO.toString(), "SUCCESS");
        return convertToDTO(updatedCustomer);
    }

    public void deleteCustomer(Long id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            auditService.createAuditEntry("DELETE", id, null, "SUCCESS");
        } else {
            auditService.createAuditEntry("DELETE", id, null, "FAILED");
            throw new CustomerNotFoundException("Customer with ID " + id + " not found");
        }
    }

    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private Customer convertToEntity(CustomerDTO customerDTO) {
        return Customer.builder()
                .name(customerDTO.getName())
                .age(customerDTO.getAge())
                .dateOfBirth(customerDTO.getDateOfBirth())
                .address(customerDTO.getAddress())
                .gender(customerDTO.getGender())
                .build();
    }

    private CustomerDTO convertToDTO(Customer customer) {
        return CustomerDTO.builder()
                .id(customer.getId())
                .name(customer.getName())
                .age(customer.getAge())
                .dateOfBirth(customer.getDateOfBirth())
                .address(customer.getAddress())
                .gender(customer.getGender())
                .build();
    }
}
