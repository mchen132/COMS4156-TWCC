package com.TWCC.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.TWCC.data.TwccUser;
import com.TWCC.payload.JwtResponse;
import com.TWCC.payload.LoginRequest;
import com.TWCC.payload.MessageResponse;
import com.TWCC.repository.UserRepository;

@TestMethodOrder(MethodOrderer.Random.class)
@SpringBootTest
@ActiveProfiles("test")
public class UserControllerIntegrationTest {
    @Autowired
    UserController userController;

    @Autowired
    UserRepository userRepository;

    private TwccUser testUser;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();

        testUser = new TwccUser(
            "Foo",
            "Bar",
            25,
            "foobar",
            "12345",
            "foobar@baz.com"
        );
    }

    @AfterEach
    void tearDown() {
        testUser = null;
        userRepository.deleteAll();
    }

    @Test
    void testRegisterUserSuccessfully() {
        // When
        ResponseEntity<?> registerUserResponse = userController.registerUser(testUser);

        try {
            // Then
            assertEquals(HttpStatus.OK, registerUserResponse.getStatusCode());
            TwccUser registeredUser = (TwccUser) registerUserResponse.getBody();
            assertEquals(testUser.getFirstName(), registeredUser.getFirstName());
            assertEquals(testUser.getLastName(), registeredUser.getLastName());
            assertEquals(testUser.getAge(), registeredUser.getAge());
            assertEquals(testUser.getUsername(), registeredUser.getUsername());
            assertEquals(testUser.getEmail(), registeredUser.getEmail());
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    void testRegisterUserWithExistingUsername() {
        // Given
        userRepository.save(testUser);
        
        TwccUser sameUsernameUser = new TwccUser(
            "Bar",
            "Baz",
            50,
            "foobar",
            "12345",
            "barbaz@foo.com"
        );

        // When
        ResponseEntity<?> registerUserResponse = userController.registerUser(sameUsernameUser);

        try {
            // Then
            assertEquals(HttpStatus.BAD_REQUEST, registerUserResponse.getStatusCode());
            MessageResponse messageResponse = (MessageResponse) registerUserResponse.getBody();
            assertEquals("Username is already being used.", messageResponse.getMessage());
            assertEquals(HttpStatus.BAD_REQUEST.value(), messageResponse.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    void testRegisterUserWithExistingEmail() {
        // Given
        userRepository.save(testUser);
        
        TwccUser sameEmailUser = new TwccUser(
            "Bar",
            "Baz",
            50,
            "barbaz",
            "12345",
            "foobar@baz.com"
        );

        // When
        ResponseEntity<?> registerUserResponse = userController.registerUser(sameEmailUser);

        try {
            // Then
            assertEquals(HttpStatus.BAD_REQUEST, registerUserResponse.getStatusCode());
            MessageResponse messageResponse = (MessageResponse) registerUserResponse.getBody();
            assertEquals("Email is already being used.", messageResponse.getMessage());
            assertEquals(HttpStatus.BAD_REQUEST.value(), messageResponse.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    void testLoginUserSuccessfully() {
        // Given
        TwccUser newUser = new TwccUser(
            "Naruto",
            "Uzumaki",
            13,
            "Naruto.Uzumaki",
            "ILoveNinetails",
            "Naruto.Uzumaki@kyuubi.com"
        );
        ResponseEntity<?> registerUserResponse = userController.registerUser(newUser);
        TwccUser actualUser = (TwccUser) registerUserResponse.getBody();

        // When
        ResponseEntity<?> loginUserResponse = userController.loginUser(
            new LoginRequest(
                newUser.getUsername(),
                "ILoveNinetails"
            )
        );

        try {
            // Then
            assertEquals(HttpStatus.OK, loginUserResponse.getStatusCode());
            JwtResponse jwtResponse = (JwtResponse) loginUserResponse.getBody();
            assertNotNull(jwtResponse.getToken());
            assertEquals(actualUser.getId(), jwtResponse.getId());
            assertEquals(actualUser.getUsername(), jwtResponse.getUsername());
            assertEquals(actualUser.getEmail(), jwtResponse.getEmail());
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    void testLoginUserWithMissingUsername() {
        // Given
        userController.registerUser(testUser);

        // When
        ResponseEntity<?> loginUserResponse = userController.loginUser(
            new LoginRequest(
                null,
                "12345"
            )
        );

        try {
            // Then
            assertEquals(HttpStatus.BAD_REQUEST, loginUserResponse.getStatusCode());
            MessageResponse messageResponse = (MessageResponse) loginUserResponse.getBody();
            assertEquals("Username or password is empty.", messageResponse.getMessage());
            assertEquals(HttpStatus.BAD_REQUEST.value(), messageResponse.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    void testLoginUserWithMissingPassword() {
        // Given
        userController.registerUser(testUser);

        // When
        ResponseEntity<?> loginUserResponse = userController.loginUser(
            new LoginRequest(
                testUser.getUsername(),
                null
            )
        );

        try {
            // Then
            assertEquals(HttpStatus.BAD_REQUEST, loginUserResponse.getStatusCode());
            MessageResponse messageResponse = (MessageResponse) loginUserResponse.getBody();
            assertEquals("Username or password is empty.", messageResponse.getMessage());
            assertEquals(HttpStatus.BAD_REQUEST.value(), messageResponse.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }
}
