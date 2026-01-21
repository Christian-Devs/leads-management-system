package com.example.leads.controller;

import com.example.leads.domain.Lead;
import com.example.leads.repository.LeadRepository;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;
import java.util.UUID;
import java.util.List;
import java.time.LocalDateTime;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/leads")
public class LeadController {

    private final LeadRepository leadRepository;

    public LeadController(LeadRepository leadRepository) {
        this.leadRepository = leadRepository;
    }

    @PostMapping
    public Mono<ResponseEntity<Lead>> createLead(@Valid @RequestBody Lead lead) {
        lead.setId(UUID.randomUUID());
        lead.setCreatedAt(LocalDateTime.now());
        leadRepository.save(lead);
        return Mono.just(
                ResponseEntity.status(HttpStatus.CREATED).body(lead));
    }

    @GetMapping
    public Mono<List<Lead>> getAllLeads(@RequestParam(required = false) String from,
            @RequestParam(required = false) String to) {
        if (from != null && to != null) {
            return Mono.just(leadRepository.findByDateRange(
                    LocalDate.parse(from).atStartOfDay(),
                    LocalDate.parse(to).atTime(23, 59, 59)));
        }
        List<Lead> leads = leadRepository.findAll();
        return Mono.just(leads);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Lead>> getLeadById(@PathVariable UUID id) {
        return Mono.just(leadRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Lead>> updateLead(@PathVariable UUID id, @Valid @RequestBody Lead lead) {
        leadRepository.update(id, lead);
        return Mono.just(ResponseEntity.noContent().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteLead(@PathVariable UUID id) {
        leadRepository.deleteById(id);
        return Mono.just(ResponseEntity.noContent().build());
    }

}
