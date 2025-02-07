package com.example.eventplanning.controller;

import com.example.eventplanning.dto.UserLoginDto;
import com.example.eventplanning.dto.UserRegistrationDto;
import com.example.eventplanning.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public String registerUser(@RequestBody UserRegistrationDto dto) {
        userService.registerUser(dto);
        return "User registered successfully";
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginDto dto) {
        String token = userService.loginUser(dto);
        return ResponseEntity.ok().body(Collections.singletonMap("token", token));
    }
}
