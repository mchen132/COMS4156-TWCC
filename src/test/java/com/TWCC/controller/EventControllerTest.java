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
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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

	Event RECORD_1 = new Event(
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
	void updateEventSuccessfully() {
		Event updatedEvent = new Event(
			1,
			"Manhattan",
			18, 
            "Finals Study Session", 
            "This is a finals study session",
            12.55,
            125.34,
            10.0f,
            "www.columbia.edu",
            new Timestamp(new Date().getTime() - 10),
            new Timestamp(new Date().getTime() + 5),
            new Timestamp(new Date().getTime() + 10)
        );

		Mockito.when(eventRepository.findById(RECORD_1.getId())).thenReturn(Optional.of(RECORD_1));
		Mockito.when(eventRepository.save(updatedEvent)).thenReturn(updatedEvent);
		try {
			MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/eventUpdate")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(this.objectMapper.writeValueAsString(updatedEvent));

		
			mockMvc.perform(mockRequest)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.address", is("Manhattan")))
				.andExpect(jsonPath("$.ageLimit", is(18)))
				.andExpect(jsonPath("$.name", is("Finals Study Session")))
				.andExpect(jsonPath("$.description", is("This is a finals study session")))
				.andExpect(jsonPath("$.longitude", is(12.55)))
				.andExpect(jsonPath("$.latitude", is(125.34)))
				.andExpect(jsonPath("$.cost", is(10.0f)))
				.andExpect(jsonPath("$.media", is("www.columbia.edu")))
				.andExpect(jsonPath("$.startTimeStamp", is(new Timestamp(new Date().getTime() + 5))))
				.andExpect(jsonPath("$.endTimeStamp", is(new Timestamp(new Date().getTime() + 10))));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	// void updateEvent_nullId() {
	// 	Event updatedEvent = new Event(
	// 		1,
	// 		"Manhattan",
	// 		18, 
    //         "Finals Study Session", 
    //         "This is a finals study session",
    //         12.55,
    //         125.34,
    //         10.0f,
    //         "www.columbia.edu",
    //         new Timestamp(new Date().getTime() - 10),
    //         new Timestamp(new Date().getTime() + 5),
    //         new Timestamp(new Date().getTime() + 10)
    //     );

	// 	try {
	// 		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/eventUpdate")
	// 			.contentType(MediaType.APPLICATION_JSON)
	// 			.accept(MediaType.APPLICATION_JSON)
	// 			.content(this.objectMapper.writeValueAsString(updatedEvent));

	// 		mockMvc.perform(mockRequest)
	// 			.andExpect((status()))

	// 	} catch (Exception e) {
	// 		e.printStackTrace();
	// 	}
	// }

	@Test
	void updateEvent_recordNotFound() throws Exception {
		Event updatedEvent = new Event(
			1000,
			"Manhattan",
			18, 
            "Finals Study Session", 
            "This is a finals study session",
            12.55,
            125.34,
            10.0f,
            "www.columbia.edu",
            new Timestamp(new Date().getTime() - 10),
            new Timestamp(new Date().getTime() + 5),
            new Timestamp(new Date().getTime() + 10)
        );

		Mockito.when(eventRepository.findById(updatedEvent.getId())).thenReturn(null);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/eventUpdate")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(this.objectMapper.writeValueAsString(updatedEvent));

			mockMvc.perform(mockRequest)
				.andExpect(status().isBadRequest())
				.andExpect(result ->
					assertTrue(result.getResolvedException() instanceof NotFoundException))
				.andExpect(result ->
					assertEquals("Event with ID 1000 does not exist.", result.getResolvedException().getMessage()));
	

	}

}
