package com.tradingjournal.backend.service.impl;

import com.tradingjournal.backend.config.JwtService;
import com.tradingjournal.backend.dto.auth.AuthResponseDTO;
import com.tradingjournal.backend.dto.auth.LoginRequestDTO;
import com.tradingjournal.backend.dto.auth.RegisterRequestDTO;
import com.tradingjournal.backend.entity.UserEntity;
import com.tradingjournal.backend.mapper.UserMapper;
import com.tradingjournal.backend.repository.UserRepository;
import com.tradingjournal.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository,
                           UserMapper userMapper,
                           JwtService jwtService,
                           AuthenticationManager authenticationManager){
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public AuthResponseDTO register(RegisterRequestDTO request) {
        // Check if user with given email already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            // You might want a custom exception here, e.g., UserAlreadyExistsException
            throw new IllegalArgumentException("User with this email already exists.");
        }

        UserEntity user = userMapper.toUserEntity(request);
        userRepository.save(user); // Save the new user to the database

        String jwtToken = jwtService.generateToken(user); // Generate JWT for the new user

        return AuthResponseDTO.builder()
                .token(jwtToken)
                .message("User registered successfully.")
                .build();
    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO request) {
        try {
            // Authenticate user using Spring Security's AuthenticationManager
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (UsernameNotFoundException | org.springframework.security.authentication.BadCredentialsException e) {
            // Catch specific authentication exceptions and re-throw with a generic message
            throw new IllegalArgumentException("Invalid email or password.");
        }

        // If authentication successful, load user details and generate JWT
        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found after authentication (should not happen)"));

        String jwtToken = jwtService.generateToken(user);

        return AuthResponseDTO.builder()
                .token(jwtToken)
                .message("Login successful.")
                .build();
    }
}
