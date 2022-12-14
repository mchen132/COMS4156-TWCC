package com.TWCC.service;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.TWCC.data.Event;

@SpringBootTest
@ActiveProfiles("test")
public class EventServiceTest {
    @Autowired
    private EventService eventService;

    private List<Event> events = new ArrayList<>();
    private Event event1, event2, event3;

    @BeforeEach
    void setup() {
        event1 = new Event(1, "Columbia", 22, 
                                "Midterm Study session", 
                                "This is a midterm study session", 
                                12.5, 122.34, 50, "www.columbia.edu", 1,
								"social, study",
                                new Timestamp(new Date().getTime() - 10), 
                                new Timestamp(new GregorianCalendar(2022, 12, 1).getTimeInMillis()),
                                new Timestamp(new GregorianCalendar(2022, 12, 2).getTimeInMillis()));
        
        event2 = new Event(2, "UW", 18, 
                                "Midterm Study session at UW", 
                                "This is a midterm study session at UW", 
                                12.5, 122.34, 15, "www.uw.edu", 2,
								"social, study",
                                new Timestamp(new Date().getTime() - 10), 
                                new Timestamp(new GregorianCalendar(2022, 12, 3).getTimeInMillis()),
                                new Timestamp(new GregorianCalendar(2022, 12, 4).getTimeInMillis()));

        event3 = new Event(3, "Columbia", 18, 
                                "Midterm Study session at UMD", 
                                "This is a midterm study session at UMD", 
                                12.5, 122.34, 10, "www.umd.edu", 3,
								"social, study",
                                new Timestamp(new Date().getTime() - 10), 
                                new Timestamp(new GregorianCalendar(2022, 12, 24).getTimeInMillis()),
                                new Timestamp(new GregorianCalendar(2022, 12, 25).getTimeInMillis()));
    
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
    void testFilterEventsByAgeLimitSuccessfully() {
        // Given
        Map<String, String> filterParams = new HashMap<String, String>() {{
            put("ageLimit", "21");
        }};

        // When
        List<Event> remainingEvents = eventService.filterEvents(filterParams, events);

        // Then
        assertEquals(2, remainingEvents.size());
        
        for (Event event: remainingEvents) {
            if (event.getId() == 2) {
                assertEquals(event2.getName(), event.getName());
                assertEquals(event2.getAddress(), event.getAddress());
                assertEquals(event2.getDescription(), event.getDescription());
                assertEquals(event2.getCategories(), event.getCategories());
            } else if (event.getId() == 3) {
                assertEquals(event3.getName(), event.getName());
                assertEquals(event3.getAddress(), event.getAddress());
                assertEquals(event3.getDescription(), event.getDescription());
                assertEquals(event3.getCategories(), event.getCategories());
            } else {
                assertTrue(false);
            }
        }
    }

    @Test
    void testFilterEventsByCostSuccessfully() {
        // Given
        Map<String, String> filterParams = new HashMap<String, String>() {{
            put("cost", "12");
        }};

        // When
        List<Event> remainingEvents = eventService.filterEvents(filterParams, events);

        // Then
        assertEquals(1, remainingEvents.size());
        assertEquals(event3.getName(), remainingEvents.get(0).getName());
        assertEquals(event3.getAddress(), remainingEvents.get(0).getAddress());
        assertEquals(event3.getDescription(), remainingEvents.get(0).getDescription());
        assertEquals(event3.getCategories(), remainingEvents.get(0).getCategories());
    }

    @Test
    void testFilterEventsByStartTimestampSuccessfully() {
        // Given
        Map<String, String> filterParams = new HashMap<String, String>() {{
            put(
                "startTimestamp",
                new Timestamp(new GregorianCalendar(2022, 12, 20)
                    .getTimeInMillis()
                ).toString()
            );
        }};

        // When
        List<Event> remainingEvents = eventService.filterEvents(filterParams, events);

        // Then
        assertEquals(1, remainingEvents.size());
        assertEquals(event3.getName(), remainingEvents.get(0).getName());
        assertEquals(event3.getAddress(), remainingEvents.get(0).getAddress());
        assertEquals(event3.getDescription(), remainingEvents.get(0).getDescription());
        assertEquals(event3.getCategories(), remainingEvents.get(0).getCategories());
    }

    @Test
    void testFilterEventsByEndTimestampSuccessfully() {
        // Given
        Map<String, String> filterParams = new HashMap<String, String>() {{
            put(
                "endTimestamp",
                new Timestamp(new GregorianCalendar(2022, 12, 3)
                    .getTimeInMillis()
                ).toString()
            );
        }};

        // When
        List<Event> remainingEvents = eventService.filterEvents(filterParams, events);

        // Then
        assertEquals(1, remainingEvents.size());
        assertEquals(event1.getName(), remainingEvents.get(0).getName());
        assertEquals(event1.getAddress(), remainingEvents.get(0).getAddress());
        assertEquals(event1.getDescription(), remainingEvents.get(0).getDescription());
        assertEquals(event1.getCategories(), remainingEvents.get(0).getCategories());
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
