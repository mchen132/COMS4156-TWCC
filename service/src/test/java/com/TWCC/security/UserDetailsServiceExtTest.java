package com.TWCC.security;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.TWCC.data.TwccUser;
import com.TWCC.repository.UserRepository;

@TestMethodOrder(MethodOrderer.Random.class)
@SpringBootTest
@ActiveProfiles("test")
public class UserDetailsServiceExtTest {
    @Autowired
    UserDetailsServiceExt userDetailsService;

    @Autowired
    UserRepository userRepository;

    private TwccUser testUser;

    @BeforeEach
    void setup() {
        testUser = userRepository.save(new TwccUser(
            "Foo",
            "Bar",
            100,
            "FooBar",
            "Baz",
            "foobar@baz.com"
        ));
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void testLoadUserByUsername() {
        // When
        UserDetailsExt userDetails = (UserDetailsExt) userDetailsService.loadUserByUsername(testUser.getUsername());

        // Then
        assertEquals("Foo", userDetails.getFirstName());
        assertEquals("Bar", userDetails.getLastName());
        assertEquals("FooBar", userDetails.getUsername());
        assertEquals("foobar@baz.com", userDetails.getEmail());
        assertEquals("Baz", userDetails.getPassword());
    }

    @Test
    void testLoadUserByUsernameWithNonExistingUsername() {
        try {
            // When
            userDetailsService.loadUserByUsername("FooBaz");
            
            // Should fall into catch statement or else fail this test
            assertTrue(false);
        } catch (Exception e) {
            // Then
            assertEquals("User not found with the username of: FooBaz", e.getMessage());
        }
    }
}
