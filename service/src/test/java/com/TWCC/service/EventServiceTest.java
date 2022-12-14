package com.TWCC.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.TWCC.data.Event;

@TestMethodOrder(MethodOrderer.Random.class)
@SpringBootTest
@ActiveProfiles("test")
public class EventServiceTest {
    @Autowired
    private EventService eventService;

    private List<Event> events = new ArrayList<>();
    private Event event1, event2, event3;

    @BeforeEach
    void setup() {
        event1 = new Event(1, "Columbia", 18, 
                                "Midterm Study session", 
                                "This is a midterm study session", 
                                12.5, 122.34, 0, "www.columbia.edu", 1,
								"social, study",
                                new Timestamp(new Date().getTime() - 10), 
                                new Timestamp(new Date().getTime() + 5),
                                new Timestamp(new Date().getTime() + 10));
        
        event2 = new Event(2, "UW", 18, 
                                "Midterm Study session at UW", 
                                "This is a midterm study session at UW", 
                                12.5, 122.34, 0, "www.uw.edu", 2,
								"social, study",
                                new Timestamp(new Date().getTime() - 10), 
                                new Timestamp(new Date().getTime() + 5), 
                                new Timestamp(new Date().getTime() + 10));

        event3 = new Event(3, "Columbia", 18, 
                                "Midterm Study session at UMD", 
                                "This is a midterm study session at UMD", 
                                12.5, 122.34, 0, "www.umd.edu", 3,
								"social, study",
                                new Timestamp(new Date().getTime() - 10), 
                                new Timestamp(new Date().getTime() + 5), 
                                new Timestamp(new Date().getTime() + 10));
    
        events.add(event1);
        events.add(event2);
        events.add(event3);
    }

    @Test
    void testFilterEventsSuccessfully() {
        // Given
        Map<String, String> filterParams = new HashMap<String, String>() {{
            put("address", "Columbia");
            put("description", "UMD");
            put("categories", "social, study");
        }};

        // When
        List<Event> remainingEvents = eventService.filterEvents(filterParams, events);

        // Then
        assertEquals(1, remainingEvents.size());
        assertEquals("Columbia", remainingEvents.get(0).getAddress());
        assertEquals("This is a midterm study session at UMD", remainingEvents.get(0).getDescription());
        assertEquals("social, study", remainingEvents.get(0).getCategories());
    }

    @Test
    void testFilterEventsWithCaseInsensitivitySuccessfully() {
        // Given
        Map<String, String> filterParams = new HashMap<String, String>() {{
            put("address", "columbia");
            put("description", "umd");
            put("name", "midterm study session");
            put("media", "wWw.UmD.eDu");
            put("categories", "social, study");
        }};

        // When
        List<Event> remainingEvents = eventService.filterEvents(filterParams, events);

        // Then
        assertEquals(1, remainingEvents.size());
        assertEquals("Columbia", remainingEvents.get(0).getAddress());
        assertEquals("This is a midterm study session at UMD", remainingEvents.get(0).getDescription());
        assertEquals("Midterm Study session at UMD", remainingEvents.get(0).getName());
        assertEquals("www.umd.edu", remainingEvents.get(0).getMedia());
        assertEquals("social, study", remainingEvents.get(0).getCategories());
    }

    @Test
    void testFilterEventsWithNoFilterParamsSpecified() {
        // Given
        Map<String, String> filterParams = new HashMap<String, String>();

        // When
        List<Event> remainingEvents = eventService.filterEvents(filterParams, events);

        // Then
        assertEquals(3, remainingEvents.size());
    }

    @Test
    void testFilterEventsWithNullFilterParams() {
        // When
        List<Event> remainingEvents = eventService.filterEvents(null, events);

        // Then
        assertEquals(0, remainingEvents.size());
    }

    @Test
    void testFilterEventsWithNullEvents() {
        // Given
        Map<String, String> filterParams = new HashMap<String, String>() {{
            put("address", "columbia");
            put("description", "umd");
            put("name", "midterm study session");
            put("media", "wWw.UmD.eDu");
            put("categories", "soCial, stUdy");
        }};

        // When
        List<Event> remainingEvents = eventService.filterEvents(filterParams, null);

        // Then
        assertEquals(0, remainingEvents.size());
    }


}
