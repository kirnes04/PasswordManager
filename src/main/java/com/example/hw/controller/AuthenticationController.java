package com.example.hw.controller;

import com.example.hw.dao.request.SignInRequest;
import com.example.hw.dao.request.SignUpRequest;
import com.example.hw.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest request) {
        try {
            var res = authenticationService.signUp(request);
            return ResponseEntity.ok(res);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.status(400).body(exception.getMessage());
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody SignInRequest request) {
        try {
            var res = authenticationService.signIn(request);
            return ResponseEntity.ok(res);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.status(400).body(exception.getMessage());
        }
    }
}
