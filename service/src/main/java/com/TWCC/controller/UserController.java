package com.TWCC.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.TWCC.data.TwccUser;
import com.TWCC.payload.JwtResponse;
import com.TWCC.payload.LoginRequest;
import com.TWCC.payload.MessageResponse;
import com.TWCC.repository.UserRepository;
import com.TWCC.security.JwtUtils;
import com.TWCC.security.UserDetailsExt;

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

    /**
     * Registers the user
     * 
     * @param newUser
     * @return ResponseEntity containing register user information
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody TwccUser newUser) {
        if (userRepository.existsByUsername(newUser.getUsername())) {
            return ResponseEntity.badRequest().body(
                new MessageResponse(
                    "Username is already being used.",
                    HttpStatus.BAD_REQUEST.value()
                )
            );
        }

        if (userRepository.existsByEmail(newUser.getEmail())) {
            return ResponseEntity.badRequest().body(
                new MessageResponse(
                    "Email is already being used.",
                    HttpStatus.BAD_REQUEST.value()
                )
            );
        }

        // Encode password
        newUser.setPassword(encoder.encode(newUser.getPassword()));

        userRepository.save(newUser);

        return ResponseEntity.ok().body(newUser);
    }

    /**
     * Login as user and gets new JWT on successful login
     * 
     * @param loginRequest
     * @return ResponseEntity containing login user response information
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        if (loginRequest.getUsername() == null || loginRequest.getPassword() == null) {        
            return ResponseEntity.badRequest().body(
                new MessageResponse(
                    "Username or password is empty",
                    HttpStatus.BAD_REQUEST.value()
                )
            );
        }

        Authentication auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwt = jwtUtils.generateJwtToken(auth);

        UserDetailsExt userDetails = (UserDetailsExt) auth.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(
            jwt,
            userDetails.getId(),
            userDetails.getUsername(),
            userDetails.getEmail()
        ));
    }
}
