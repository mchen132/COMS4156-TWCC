package com.TWCC.security;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.TWCC.data.TwccUser;
import com.TWCC.repository.UserRepository;

@SpringBootTest
@ActiveProfiles("test")
public class UserDetailsServiceExtTest {
    @Autowired
    UserDetailsServiceExt userDetailsService;

    @Autowired
    UserRepository userRepository;

    @Test
    void testLoadUserByUsername() {
        // Given
        TwccUser testUser = userRepository.save(new TwccUser(
            "Foo",
            "Bar",
            100,
            "FooBar",
            "Baz",
            "foobar@baz.com"
        ));

        // When
        UserDetailsExt userDetails = (UserDetailsExt) userDetailsService.loadUserByUsername(testUser.getUsername());

        // Then
        assertEquals(1, userDetails.getId());
        assertEquals("Foo", userDetails.getFirstName());
        assertEquals("Bar", userDetails.getLastName());
        assertEquals("FooBar", userDetails.getUsername());
        assertEquals("foobar@baz.com", userDetails.getEmail());
        assertEquals("Baz", userDetails.getPassword());
    }

    @Test
    void testLoadUserByUsernameWithNonExistingUsername() {
        // Given
        userRepository.save(new TwccUser(
            "Foo",
            "Bar",
            100,
            "FooBar",
            "Baz",
            "foobar@baz.com"
        ));

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
