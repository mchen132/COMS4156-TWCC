package com.TWCC.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.init.ScriptException;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;

import com.TWCC.data.TwccUser;
import com.TWCC.payload.LoginRequest;
import com.TWCC.repository.UserRepository;
import com.TWCC.security.JwtUtils;
import com.TWCC.security.UserDetailsExt;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserRepository userRepository;

    @MockBean
    AuthenticationManager authenticationManager;

    @MockBean
    PasswordEncoder encoder;

    @MockBean
    JwtUtils jwtUtils;

    private TwccUser testUser;
    private static String CSRF_TOKEN_NAME;
	private static HttpSessionCsrfTokenRepository csrfTokenRepo;

    @BeforeAll
    static void beforeClass() throws ScriptException, SQLException {
        // CSRF Token Setup
		CSRF_TOKEN_NAME = "org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN";
		csrfTokenRepo = new HttpSessionCsrfTokenRepository();
    }

    @BeforeEach
    void setUp() throws SQLException {
        testUser = new TwccUser(
            1,
            "foo",
            "bar",
            25,
            "foobar",
            "12345",
            "foobar@baz.com"
        );
    }

    @AfterEach
    void tearDown() {
        
    }

    @AfterAll
    static void afterClass() {
        // TODO: cleanup after all tests
    }

    @Test
    @WithMockUser
    void testRegisterUserSuccessfully() {
        Mockito.when(userRepository.save(any())).thenReturn(testUser);

        try {
            CsrfToken csrfToken = (CsrfToken) csrfTokenRepo.generateToken(new MockHttpServletRequest());
            MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/user/register")
                    .param("_csrf", csrfToken.getToken())
					.sessionAttr(CSRF_TOKEN_NAME, csrfToken) // Need to pass CSRF Token for POST requests
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.content(this.objectMapper.writeValueAsString(ResponseEntity.ok().body(testUser)));

            mockMvc.perform(mockRequest)
                .andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @WithMockUser
    void testRegisterUserWithExistingUsername() {
        Mockito.when(userRepository.existsByUsername("foobar")).thenReturn(true);

        try {
            CsrfToken csrfToken = (CsrfToken) csrfTokenRepo.generateToken(new MockHttpServletRequest());
            MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/user/register")
                    .param("_csrf", csrfToken.getToken())
					.sessionAttr(CSRF_TOKEN_NAME, csrfToken) // Need to pass CSRF Token for POST requests
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.content(this.objectMapper.writeValueAsString(testUser));

            mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.message", is("Username is already being used.")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @WithMockUser
    void testRegisterUserWithExistingEmail() {
        Mockito.when(userRepository.existsByEmail("foobar@baz.com")).thenReturn(true);

        try {
            CsrfToken csrfToken = (CsrfToken) csrfTokenRepo.generateToken(new MockHttpServletRequest());
            MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/user/register")
                    .param("_csrf", csrfToken.getToken())
					.sessionAttr(CSRF_TOKEN_NAME, csrfToken) // Need to pass CSRF Token for POST requests
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.content(this.objectMapper.writeValueAsString(testUser));

            mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.message", is("Email is already being used.")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @WithMockUser
    void testLoginUserSuccessfully() {
        LoginRequest loginRequest = new LoginRequest("testuser", "12345");
        Mockito.when(jwtUtils.generateJwtToken(any())).thenReturn("jwt-token");
        Mockito.when(authenticationManager.authenticate(any())).thenReturn(new MockAuthentication());

        try {
            CsrfToken csrfToken = (CsrfToken) csrfTokenRepo.generateToken(new MockHttpServletRequest());
            MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/user/login")
                    .param("_csrf", csrfToken.getToken())
					.sessionAttr(CSRF_TOKEN_NAME, csrfToken) // Need to pass CSRF Token for POST requests
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.content(this.objectMapper.writeValueAsString(loginRequest));

            mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.token", is("jwt-token")))
                .andExpect(jsonPath("$.id", is(testUser.getId())))
                .andExpect(jsonPath("$.username", is(testUser.getUsername())))
                .andExpect(jsonPath("$.email", is(testUser.getEmail())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @WithMockUser
    void testLoginUserWithMissingUsername() {
        Mockito.when(jwtUtils.generateJwtToken(any())).thenReturn("jwt-token");
        Mockito.when(authenticationManager.authenticate(any())).thenReturn(new MockAuthentication());

        try {
            CsrfToken csrfToken = (CsrfToken) csrfTokenRepo.generateToken(new MockHttpServletRequest());
            MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/user/login")
                    .param("_csrf", csrfToken.getToken())
					.sessionAttr(CSRF_TOKEN_NAME, csrfToken) // Need to pass CSRF Token for POST requests
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.content(this.objectMapper.writeValueAsString(
                        new HashMap<String, Object>() {{					
						    put("password", "12345");
                        }}
                    ));

            mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.message", is("Username or password is empty.")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @WithMockUser
    void testLoginUserWithMissingPassword() {
        Mockito.when(jwtUtils.generateJwtToken(any())).thenReturn("jwt-token");
        Mockito.when(authenticationManager.authenticate(any())).thenReturn(new MockAuthentication());

        try {
            CsrfToken csrfToken = (CsrfToken) csrfTokenRepo.generateToken(new MockHttpServletRequest());
            MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/user/login")
                    .param("_csrf", csrfToken.getToken())
					.sessionAttr(CSRF_TOKEN_NAME, csrfToken) // Need to pass CSRF Token for POST requests
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.content(this.objectMapper.writeValueAsString(
                        new HashMap<String, Object>() {{					
						    put("username", "testuser");
                        }}
                    ));

            mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.message", is("Username or password is empty.")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class MockAuthentication implements Authentication {
        @Override
        public Object getPrincipal() {
            return new UserDetailsExt(
                testUser.getId(),
                testUser.getFirstName(),
                testUser.getLastName(),
                testUser.getUsername(),
                testUser.getEmail(),
                testUser.getPassword()
            );
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return null;
        }

        @Override
        public Object getCredentials() {
            return null;
        }

        @Override
        public Object getDetails() {
            return null;
        }

        @Override
        public boolean isAuthenticated() {
            return false;
        }

        @Override
        public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
            
        }
    }
}
