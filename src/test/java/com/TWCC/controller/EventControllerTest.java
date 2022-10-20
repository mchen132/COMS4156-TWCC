package com.TWCC.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Timestamp;
import java.util.Date;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.hamcrest.Matchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.TWCC.data.Event;
import com.TWCC.repository.EventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(EventController.class)
class EventControllerTest {
	@Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    EventRepository eventRepository;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void createEventSuccessfully() {
		Event event1 = new Event(
			1,
			"Columbia",
			18, 
            "Midterm Study session", 
            "This is a midterm study session",
            12.5,
            122.34,
            5.0f,
            "www.columbia.edu",
            new Timestamp(new Date().getTime() - 10),
            new Timestamp(new Date().getTime() + 5),
            new Timestamp(new Date().getTime() + 10)
        );
		
		Mockito.when(eventRepository.save(event1)).thenReturn(event1);
		
		
		try {
			MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/events")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.content(this.objectMapper.writeValueAsString(event1));
			
			mockMvc.perform(mockRequest)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.notNullValue()))
	            .andExpect(jsonPath("$.name", Matchers.is("Columbia")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
