package com.tradingjournal.backend.service;

import com.tradingjournal.backend.dto.auth.AuthResponseDTO;
import com.tradingjournal.backend.dto.auth.LoginRequestDTO;
import com.tradingjournal.backend.dto.auth.RegisterRequestDTO;

public interface AuthService {
    AuthResponseDTO register(RegisterRequestDTO request);

    AuthResponseDTO login(LoginRequestDTO request);
}
