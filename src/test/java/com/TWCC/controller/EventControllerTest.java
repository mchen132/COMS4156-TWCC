package com.TWCC.controller;

import java.util.ArrayList;
import java.util.Arrays;
import static org.mockito.ArgumentMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.http.MediaType;

import com.TWCC.data.Event;
import com.TWCC.repository.EventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(EventController.class)
public class EventControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    EventRepository eventRepository;

    private List<Event> events = new ArrayList<>();
    private Event event1, event2, event3;

    @BeforeAll
    static void beforeClass() {
        // TODO: setup before all tests
    }

    @BeforeEach
    void setUp() {
        event1 = new Event(1, "Columbia", 18, 
                                "Midterm Study session", 
                                "This is a midterm study session", 
                                12.5, 122.34, 0, "www.columbia.edu", 
                                new Timestamp(new Date().getTime() - 10), 
                                new Timestamp(new Date().getTime() + 5), 
                                new Timestamp(new Date().getTime() + 10));
        
        event2 = new Event(2, "UW", 18, 
                                "Midterm Study session at UW", 
                                "This is a midterm study session at UW", 
                                12.5, 122.34, 0, "www.uw.edu", 
                                new Timestamp(new Date().getTime() - 10), 
                                new Timestamp(new Date().getTime() + 5), 
                                new Timestamp(new Date().getTime() + 10));

        event3 = new Event(3, "Columbia", 18, 
                                "Midterm Study session at UMD", 
                                "This is a midterm study session at UMD", 
                                12.5, 122.34, 0, "www.umd.edu", 
                                new Timestamp(new Date().getTime() - 10), 
                                new Timestamp(new Date().getTime() + 5), 
                                new Timestamp(new Date().getTime() + 10));
    
        events.add(event1);
        events.add(event2);
        events.add(event3);

    }

    @AfterEach
    void tearDown() {
        events.clear();
    }

    @AfterAll
    static void afterClass() {
        // TODO: cleanup after all tests
    }

    @Test 
    void testHello() {
        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .get("/hello"))
                            .andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

    @Test
    void testGetEvents() {

        Mockito.when(eventRepository.findAll()).thenReturn(events);

        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/events"))
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$", Matchers.hasSize(3)))
                            .andExpect(jsonPath("$[2].address", Matchers.is("Columbia")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testGetEventsByAddress() {
        Mockito.when(eventRepository.findByAddress("Columbia")).thenReturn(new ArrayList<>(Arrays.asList(event1, event3)));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/events/byaddress/Columbia");
        
        try {
            mockMvc.perform(request)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", Matchers.hasSize(2)))
                    .andExpect(jsonPath("$[0].id", Matchers.is(1)))
                    .andExpect(jsonPath("$[1].id", Matchers.is(3)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testGetEventsById() {
        Mockito.when(eventRepository.findById(event1.getId())).thenReturn(java.util.Optional.of(event1));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/events/1");
        
        try {
            mockMvc.perform(request)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", Matchers.notNullValue()))
                    .andExpect(jsonPath("$.address", Matchers.is("Columbia")));
        } catch (Exception e) {
            e.printStackTrace();
        }
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
			System.out.println(this.objectMapper.writeValueAsString(event1));
			
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
	
}
