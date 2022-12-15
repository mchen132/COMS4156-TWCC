package com.TWCC.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.TWCC.data.Event;
import com.TWCC.data.TwccUser;
import com.TWCC.payload.MessageResponse;
import com.TWCC.repository.EventRepository;
import com.TWCC.repository.UserRepository;
import com.TWCC.service.EventService;
import com.TWCC.payload.LoginRequest;
import com.TWCC.payload.JwtResponse;

@TestMethodOrder(MethodOrderer.Random.class)
@SpringBootTest
@ActiveProfiles("test")
@SuppressWarnings({"checkstyle:AvoidInlineConditionals", "checkstyle:LineLengthCheck", "checkstyle:StaticVariableNameCheck", "checkstyle:MagicNumberCheck", "checkstyle:VisibilityModifierCheck", "checkstyle:FileTabCharacterCheck"})
public class EventControllerIntegrationTest {
    @Autowired
    private EventController eventController;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventService eventService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserController userController;

    private List<Event> events = new ArrayList<>();
    private Event event1, event2, event3;

	@BeforeEach
    void setUp() {
        event1 = new Event(1, "Columbia", 18,
            "Midterm Study session",
            "This is a midterm study session",
            12.5, 122.34, 0, "www.columbia.edu", 1,
            "social, study",
            new Timestamp(new Date().getTime() - 10),
            new Timestamp(new Date().getTime() + 5),
            new Timestamp(new Date().getTime() + 10)
        );

        event2 = new Event(2, "UW", 18,
            "Midterm Study session at UW",
            "This is a midterm study session at UW",
            12.5, 122.34, 0, "www.uw.edu", 2,
            "social, study",
            new Timestamp(new Date().getTime() - 10),
            new Timestamp(new Date().getTime() + 5),
            new Timestamp(new Date().getTime() + 10)
        );

        event3 = new Event(3, "Columbia", 18,
            "Midterm Study session at UMD",
            "This is a midterm study session at UMD",
            12.5, 122.34, 0, "www.umd.edu", 3,
            "social, study",
            new Timestamp(new Date().getTime() - 10),
            new Timestamp(new Date().getTime() + 5),
            new Timestamp(new Date().getTime() + 10)
        );

        events.add(event1);
        events.add(event2);
        events.add(event3);

    }

    @AfterEach
    void tearDown() {
        events.clear();
        eventRepository.deleteAll();
    }

    @Test
    void testGetEvents() {
        // Given
        eventRepository.saveAll(events);

        // When
        List<Event> events = eventController.getEvents();

        // Then
        assertEquals(3, events.size());
        assertEquals("Midterm Study session", events.get(0).getName());
        assertEquals("Midterm Study session at UW", events.get(1).getName());
        assertEquals("Midterm Study session at UMD", events.get(2).getName());
    }

    @Test
    void testGetEventsByIdSuccessfully() {
        // Given
        eventRepository.saveAll(events);
        Event event = eventRepository.findAll().get(1);
        int eventId = event.getId();

        // When
        ResponseEntity<?> eventResponse = eventController.getEventsById(eventId);

        try {
            // Then
            assertEquals(HttpStatus.OK, eventResponse.getStatusCode());
            Optional<Event> optionalEventResponse = (Optional<Event>) eventResponse.getBody();
            assertEquals(event.getName(), optionalEventResponse.get().getName());
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    void testGetEventsByIdNotFound() {
        // Given
        eventRepository.saveAll(events);

        // When
        ResponseEntity<?> eventResponse = eventController.getEventsById(500);

        try {
            // Then
            assertEquals(HttpStatus.NOT_FOUND, eventResponse.getStatusCode());
            MessageResponse messageResponse = (MessageResponse) eventResponse.getBody();
            assertEquals("Event ID: 500 does not exist", messageResponse.getMessage());
            assertEquals(HttpStatus.NOT_FOUND.value(), messageResponse.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    void testGetEventsByAddress() {
        // Given
        eventRepository.saveAll(events);

        // When
        List<Event> events = eventController.getEventsByAddress(event2.getAddress());

        //Then
        assertEquals(1, events.size());
        assertEquals(event2.getName(), events.get(0).getName());
    }

    @Test
    void testFilterEvent() {
        // Given
        eventRepository.saveAll(events);

        // When
        Map<String, String> allParams = new HashMap<String, String>();
        allParams.put("address", "Columbia");
        List<Event> filteredEvents = eventService.filterEvents(allParams, events);

        assertEquals(2, filteredEvents.size());
        assertEquals(allParams.get("address"), filteredEvents.get(0).getAddress());
        assertEquals(allParams.get("address"), filteredEvents.get(1).getAddress());
    }

    @Test
    void testCreateEventSuccessfully() {
        // Given
        Event newEvent = new Event(4, "NYU", 22,
            "test session for course 4156",
            "This is the test session for course 4156",
            12.5, 122.34, 0, "www.google.com", 4,
            "social, study",
            new Timestamp(new Date().getTime() - 10),
            new Timestamp(new Date().getTime() + 5),
            new Timestamp(new Date().getTime() + 10)
        );

        TwccUser newUser = new TwccUser(
            1,
            "foo",
            "bar",
            25,
            "foobar",
            "12345",
            "foobar@baz.com"
        );

        userController.registerUser(newUser);
        ResponseEntity<?> loginResponse = userController.loginUser(new LoginRequest(newUser.getUsername(), "12345"));
        JwtResponse jwtResponse = (JwtResponse) loginResponse.getBody();
        System.out.println(jwtResponse.getToken());

        // When
        ResponseEntity<?> eventResponse = eventController.createEvent(newEvent, "Bearer " + jwtResponse.getToken());

        try {
            // Then
            assertEquals(HttpStatus.OK, eventResponse.getStatusCode());
            Event createdEvent = (Event) eventResponse.getBody();
            assertEquals(newEvent.getName(), createdEvent.getName());
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    void testDeleteEventByIdSuccessfully() {
        // Given
        eventRepository.saveAll(events);
        int eventId = eventRepository.findAll().get(0).getId();

        // When
        ResponseEntity<?> eventResponse = eventController.deleteEventById(eventId);

        try {
            // Then
            assertEquals(HttpStatus.OK, eventResponse.getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    void testDeleteEventByIdNotFound() {
        // Given
        eventRepository.saveAll(events);
        int nonExistentEventId = eventRepository.findAll().get(2).getId() + 100;

        // When
        ResponseEntity<?> eventResponse = eventController.deleteEventById(nonExistentEventId);

        try {
            // Then
            assertEquals(HttpStatus.NOT_FOUND, eventResponse.getStatusCode());
            MessageResponse messageResponse = (MessageResponse) eventResponse.getBody();
            assertEquals("Event ID: " + nonExistentEventId + " does not exist", messageResponse.getMessage());
            assertEquals(HttpStatus.NOT_FOUND.value(), messageResponse.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    void testUpdateEventSuccessfully() {
        // Given
        eventRepository.saveAll(events);
        int eventId = eventRepository.findAll().get(0).getId();

        // When
        HashMap<String, String> jsonObject = new HashMap<String, String>();
        jsonObject.put("id", String.valueOf(eventId));
        jsonObject.put("description", "This is not a midterm study session");
        ResponseEntity<?> eventResponse = eventController.updateEvent(jsonObject);

        try {
            // Then
            assertEquals(HttpStatus.OK, eventResponse.getStatusCode());
            Optional<Event> optionalEventResponse = (Optional<Event>) eventResponse.getBody();
            assertEquals(jsonObject.get("description"), optionalEventResponse.get().getDescription());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testUpdateEventRecordNotFound() {
        // Given
        eventRepository.saveAll(events);
        int eventId = eventRepository.findAll().get(0).getId();
        eventRepository.deleteById(eventId);

        // When
        HashMap<String, String> jsonObject = new HashMap<String, String>();
        jsonObject.put("id", String.valueOf(eventId));
        jsonObject.put("description", "This is not a midterm study session");

        ResponseEntity<?> eventResponse = eventController.updateEvent(jsonObject);

        try {
            // Then
            assertEquals(HttpStatus.NOT_FOUND, eventResponse.getStatusCode());
            MessageResponse messageResponse = (MessageResponse) eventResponse.getBody();
            assertEquals("Event ID: " + eventId + " does not exist", messageResponse.getMessage());
            assertEquals(HttpStatus.NOT_FOUND.value(), messageResponse.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }
}
