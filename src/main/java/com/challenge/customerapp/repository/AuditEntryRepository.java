package com.challenge.customerapp.repository;

import com.challenge.customerapp.model.AuditEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditEntryRepository extends JpaRepository<AuditEntry, Long> {
}
