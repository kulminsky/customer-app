package com.challenge.customerapp.service;

import com.challenge.customerapp.model.AuditEntry;
import com.challenge.customerapp.repository.AuditEntryRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class AuditService {

    private final AuditEntryRepository auditEntryRepository;

    public void createAuditEntry(String action, Long customerId, String request, String status) {
        AuditEntry auditEntry = AuditEntry.builder()
                .action(action)
                .customerId(customerId)
                .request(request)
                .status(status)
                .creationDatetime(LocalDateTime.now())
                .build();
        auditEntryRepository.save(auditEntry);
    }

}
