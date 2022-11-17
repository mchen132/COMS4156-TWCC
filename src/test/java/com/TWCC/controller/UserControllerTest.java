package com.TWCC.controller;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.sql.SQLException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptException;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
// import org.testcontainers.containers.MySQLContainer;
// import org.testcontainers.junit.jupiter.Testcontainers;

import static org.mockito.ArgumentMatchers.any;

import com.TWCC.NycTodoServiceApplication;
import com.TWCC.data.User;
import com.TWCC.repository.UserRepository;
import com.TWCC.security.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(UserController.class)
@ActiveProfiles("test")
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

    private User testUser;
    private static String CSRF_TOKEN_NAME;
	private static HttpSessionCsrfTokenRepository csrfTokenRepo;

    // SQL Scripts
    private static final String CREATE_USER_T_SQL_SCRIPT = "scripts/create/user_t.sql";

    @BeforeAll
    static void beforeClass() throws ScriptException, SQLException {
        // CSRF Token Setup
		CSRF_TOKEN_NAME = "org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN";
		csrfTokenRepo = new HttpSessionCsrfTokenRepository();
    }

    @BeforeEach
    void setUp() throws SQLException {
        testUser = new User(
            1,
            "foo",
            "bar",
            25,
            "foobar",
            "12345",
            "foobar@baz.com"
        );
        // ScriptUtils.executeSqlScript(jdbc.getDataSource().getConnection(), new ClassPathResource(CREATE_USER_T_SQL_SCRIPT));
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
        // Mockito.when(userRepository.save(any())).thenReturn(testUser);

        User existingUser = new User(
            2,
            "Wesley",
            "Wei",
            25,
            "foobar",
            "12345",
            "ww2623@columbia.edu"
        );

        userRepository.save(existingUser);
        System.out.println("PRINTING FIND ALL USERS!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println(userRepository.findAll().toString());

        try {
            CsrfToken csrfToken = (CsrfToken) csrfTokenRepo.generateToken(new MockHttpServletRequest());
            MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/user/register")
                    .param("_csrf", csrfToken.getToken())
					.sessionAttr(CSRF_TOKEN_NAME, csrfToken) // Need to pass CSRF Token for POST requests
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.content(this.objectMapper.writeValueAsString(testUser));

            MvcResult result = mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andReturn();
                // .andExpect(jsonPath("$", notNullValue()))
                // .andExpect(jsonPath("$.message", is("Username is already being used.")));
            System.out.println(result.getResponse().getContentAsString());
            // UserController userController = new UserController();
            // userController.registerUser(testUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
