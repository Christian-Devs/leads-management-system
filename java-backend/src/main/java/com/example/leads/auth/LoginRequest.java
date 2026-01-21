package com.example.leads.auth;

public record LoginRequest(
        String username,
        String password) {
}