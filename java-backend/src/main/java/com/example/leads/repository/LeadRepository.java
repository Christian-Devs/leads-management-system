package com.example.leads.repository;

import com.example.leads.domain.Lead;
import java.util.UUID;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

public interface LeadRepository {

    Lead save(Lead lead);

    Optional<Lead> findById(UUID id);

    List<Lead> findAll();

    void deleteById(UUID id);

    List<Lead> findByDateRange(LocalDateTime from, LocalDateTime to);

    void update(UUID id, Lead lead);
}
