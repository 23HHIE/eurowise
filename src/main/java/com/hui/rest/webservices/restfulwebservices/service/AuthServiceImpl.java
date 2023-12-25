package com.hui.rest.webservices.restfulwebservices.service;


import com.hui.rest.webservices.restfulwebservices.dto.AuthResponse;
import com.hui.rest.webservices.restfulwebservices.dto.LoginRequest;
import com.hui.rest.webservices.restfulwebservices.dto.RegisterRequest;
import com.hui.rest.webservices.restfulwebservices.expense.repository.UserRepository;
import com.hui.rest.webservices.restfulwebservices.security.JwtService;
import com.hui.rest.webservices.restfulwebservices.user.User;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthServiceImpl implements AuthService {

    @Resource
    private UserRepository userRepository;
    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private JwtService jwtService;

    @Override
    public AuthResponse register(RegisterRequest registerRequest) {
        User user = new User();
        BeanUtils.copyProperties(registerRequest, user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        user = userRepository.save(user);
        var authResponse = new AuthResponse();
        BeanUtils.copyProperties(user, authResponse);
        String jwtToken = jwtService.generateToken(user);
        authResponse.setToken(jwtToken);
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        registerRequest.getUsername(),
                        registerRequest.getPassword()
                )
        );
        return authResponse;
    }

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        User user = userRepository.findByName(loginRequest.getUsername());
        String jwtToken = jwtService.generateToken(user);
        AuthResponse authResponse = new AuthResponse();
        BeanUtils.copyProperties(user, authResponse);
        authResponse.setToken(jwtToken);
        return authResponse;
    }
}