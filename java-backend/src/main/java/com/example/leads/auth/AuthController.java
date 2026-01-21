package com.example.leads.auth;

import com.example.leads.security.jwt.JwtUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<Map<String, String>>> login(
            @RequestBody LoginRequest loginRequest) {
        if (!"admin".equals(loginRequest.username()) ||
                !"password".equals(loginRequest.password())) {
            return Mono.just(ResponseEntity.status(401).build());
        }

        String token = jwtUtil.generateToken(loginRequest.username());
        return Mono.just(ResponseEntity.ok(Map.of("token", token)));
    }
}
