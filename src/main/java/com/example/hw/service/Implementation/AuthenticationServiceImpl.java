package com.example.hw.service.Implementation;

import com.example.hw.dao.request.SignInRequest;
import com.example.hw.dao.request.SignUpRequest;
import com.example.hw.dao.response.JwtAuthenticationResponse;
import com.example.hw.entities.Record;
import com.example.hw.entities.User;
import com.example.hw.repository.RecordRepository;
import com.example.hw.repository.UserRepository;
import com.example.hw.service.AuthenticationService;
import com.example.hw.service.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationServiceImpl(UserRepository userRepository,
                                     PasswordEncoder passwordEncoder,
                                     JwtService jwtService,
                                     AuthenticationManager authenticationManager
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public JwtAuthenticationResponse signUp(SignUpRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User with email " + request.getEmail() + " already exists.");
        }
        User user = new User(0,
                request.getEmail(),
                request.getLogin(),
                passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }

    @Override
    public JwtAuthenticationResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(),
                        request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid name or password."));
        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }
}
