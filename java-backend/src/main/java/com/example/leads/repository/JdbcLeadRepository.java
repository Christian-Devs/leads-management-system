package com.example.leads.repository;

import com.example.leads.domain.Lead;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import java.util.UUID;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import java.sql.Timestamp;

@Repository
public class JdbcLeadRepository implements LeadRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcLeadRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Lead> leadRowMapper = (rs, rowNum) -> {
        Lead lead = new Lead();
        lead.setId(UUID.fromString(rs.getString("id")));
        lead.setName(rs.getString("name"));
        lead.setEmail(rs.getString("email"));
        lead.setPhone(rs.getString("phone"));
        lead.setSource(rs.getString("source"));
        lead.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return lead;
    };

    @Override
    public Lead save(Lead lead) {
        String sql = "INSERT INTO leads (id, name, email, phone, source, created_at) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, lead.getId(), lead.getName(), lead.getEmail(), lead.getPhone(), lead.getSource(),
                Timestamp.valueOf(lead.getCreatedAt()));
        return lead;
    }

    @Override
    public List<Lead> findAll() {
        String sql = "SELECT * FROM leads ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, leadRowMapper);
    }

    @Override
    public Optional<Lead> findById(UUID id) {
        String sql = "SELECT * FROM leads WHERE id = ?";
        List<Lead> leads = jdbcTemplate.query(sql, leadRowMapper, id);
        if (leads.isEmpty()) {
            return Optional.empty();
        } else {
            return leads.stream().findFirst();
        }
    }

    @Override
    public List<Lead> findByDateRange(LocalDateTime from, LocalDateTime to) {
        String sql = "SELECT * FROM leads WHERE created_at BETWEEN ? AND ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, leadRowMapper, Timestamp.valueOf(from), Timestamp.valueOf(to));
    }

    @Override
    public void update(UUID id, Lead lead) {
        String sql = "UPDATE leads SET name = ?, email = ?, phone = ?, source = ? WHERE id = ?";
        jdbcTemplate.update(sql, lead.getName(), lead.getEmail(), lead.getPhone(), lead.getSource(), id);
    }

    @Override
    public void deleteById(UUID id) {
        String sql = "DELETE FROM leads WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

}
