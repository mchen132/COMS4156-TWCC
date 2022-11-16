package com.TWCC.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.TWCC.data.User;
import com.TWCC.repository.UserRepository;
import com.TWCC.security.JwtUtils;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    AuthenticationManager authenticationManager;
    
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User newUser) {
        if (userRepository.existsByUsername(newUser.getUsername())) {
            return ResponseEntity.badRequest().body(
                "Username is already being used."
            );
        }

        if (userRepository.existsByEmail(newUser.getEmail())) {
            return ResponseEntity.badRequest().body(
                "Email is already being used."
            );
        }

        // Encode password
        newUser.setPassword(encoder.encode(newUser.getPassword()));

        userRepository.save(newUser);

        return ResponseEntity.ok().body(newUser);
    }
}
