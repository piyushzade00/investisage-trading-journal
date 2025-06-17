package com.tradingjournal.backend.controller;

import com.tradingjournal.backend.dto.auth.AuthResponseDTO;
import com.tradingjournal.backend.dto.auth.LoginRequestDTO;
import com.tradingjournal.backend.dto.auth.RegisterRequestDTO;
import com.tradingjournal.backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
        try {
            AuthResponseDTO response = authService.register(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // Return 400 Bad Request if user already exists or validation fails
            return ResponseEntity.badRequest().body(AuthResponseDTO.builder().message(e.getMessage()).build());
        } catch (Exception e) {
            // Generic error for other issues
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(AuthResponseDTO.builder().message("Registration failed: " + e.getMessage()).build());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        try {
            AuthResponseDTO response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // Return 401 Unauthorized for invalid credentials
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(AuthResponseDTO.builder().message(e.getMessage()).build());
        } catch (Exception e) {
            // Generic error for other issues
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(AuthResponseDTO.builder().message("Login failed: " + e.getMessage()).build());
        }
    }

}
