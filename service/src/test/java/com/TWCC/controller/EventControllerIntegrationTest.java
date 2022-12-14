package com.TWCC.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.TWCC.data.Event;
import com.TWCC.payload.MessageResponse;
import com.TWCC.repository.EventRepository;

@SpringBootTest
@ActiveProfiles("test")
@SuppressWarnings({"checkstyle:AvoidInlineConditionals", "checkstyle:LineLengthCheck", "checkstyle:StaticVariableNameCheck", "checkstyle:MagicNumberCheck", "checkstyle:VisibilityModifierCheck", "checkstyle:FileTabCharacterCheck"})
public class EventControllerIntegrationTest {
    @Autowired
    EventController eventController;

    @Autowired
    EventRepository eventRepository;

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

        // When
        ResponseEntity<?> response = eventController.getEventsById(event1.getId());

        try {
            // Then
            assertEquals(HttpStatus.OK, response.getStatusCode());
            Optional<Event> eventResponse = (Optional<Event>) response.getBody();
            assertEquals(event1.getName(), eventResponse.get().getName());
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
        ResponseEntity<?> response = eventController.getEventsById(5);

        try {
            // Then
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            MessageResponse messageResponse = (MessageResponse) response.getBody();
            assertEquals("Event ID: 5 does not exist", messageResponse.getMessage());
            assertEquals(HttpStatus.NOT_FOUND.value(), messageResponse.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }
}
