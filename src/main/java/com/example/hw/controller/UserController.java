package com.example.hw.controller;

import com.example.hw.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // For testing only
//    @GetMapping
//    ResponseEntity<?> getAllUsers() {
//        return ResponseEntity.ok(userService.getAllUsers());
//    }
//
//    @GetMapping("/now")
//    ResponseEntity<?> GetCurrentTime() {
//        return ResponseEntity.ok(LocalDateTime.now());
//    }
}
