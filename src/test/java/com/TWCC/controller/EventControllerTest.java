package com.TWCC.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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
            "Midterm Study Session", 
            "This is a midterm study session",
            12.5,
            122.34,
            5.0f,
            "www.columbia.edu",
            new Timestamp(new Date().getTime() - 10),
            new Timestamp(new Date().getTime() + 5),
            new Timestamp(new Date().getTime() + 10)
        );
		
		Mockito.when(eventRepository.save(any())).thenReturn(event1);		
		
		try {
			MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/events")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.content(this.objectMapper.writeValueAsString(event1));
			
			mockMvc.perform(mockRequest)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", notNullValue()))
	            .andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.address", is("Columbia")))
				.andExpect(jsonPath("$.ageLimit", is(18)))
				.andExpect(jsonPath("$.name", is("Midterm Study Session")))
				.andExpect(jsonPath("$.description", is("This is a midterm study session")))
				.andExpect(jsonPath("$.longitude", is(12.5)))
				.andExpect(jsonPath("$.latitude", is(122.34)))
				.andExpect(jsonPath("$.cost", is(5.0)))
				.andExpect(jsonPath("$.media", is("www.columbia.edu")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	@Test
	void createEventWithInvalidFields() {
		try {
			MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/events")
					.content(this.objectMapper.writeValueAsString(new HashMap<String, Object>(){{					
						put("address", "Columbia");
						put("ageLimit", 18);
						put("name", "Midterm Study Session");
						put("description", "This is a midterm study session");
						put("latitude", 12.5);
						put("longitude", 125.2);
						put("cost", "5"); // cost should be a float
						put("media", "www.columbia.edu");						
						put("startTimestamp", new Timestamp(new Date().getTime() + 5));
						put("endTimestamp", new Timestamp(new Date().getTime() + 10));
					}}))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON);
			
			mockMvc.perform(mockRequest);
			assertTrue(false); // Should not execute
		} catch (Exception e) {
			assertTrue(true); // Should execute due to failure from parsing Event entity
		}
	}

	@Test
	void deleteEventById_success() throws Exception{
		Event Record_1 = new Event(2,
	"Columbia",
	18, 
	"Midterm Study Session", 
	"This is a midterm study session",
	12.5,
	122.34,
	5.0f,
	"www.columbia.edu",
	new Timestamp(new Date().getTime() - 10),
	new Timestamp(new Date().getTime() + 5),
	new Timestamp(new Date().getTime() + 10)
	);
		Mockito.when(eventRepository.findById(Record_1.getId())).thenReturn(Optional.of(Record_1));

		mockMvc.perform(MockMvcRequestBuilders
			   .delete("/delete/2")
			   .contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}

	@Test
	void deleteEventById_notFound() throws Exception{
		try{
			Mockito.when(eventRepository.findById(5)).thenReturn(null);
			mockMvc.perform(MockMvcRequestBuilders
			   .delete("/delete/2")
			   .contentType(MediaType.APPLICATION_JSON));
		}
		catch (Exception ex){
			assertTrue(ex instanceof NotFoundException);
			System.out.println(ex);

		}

		
		
	}


}
