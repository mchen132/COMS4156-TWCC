package com.TWCC.service;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.TWCC.data.Event;

@SpringBootTest
public class EventStatisticServiceTest {
    private static EventStatisticService eventStatisticService;
    private List<Event> events;
    
    @BeforeAll
    static void beforeClass() {
        eventStatisticService = new EventStatisticService();
    }

    @BeforeEach
    void setup() {
        events = new ArrayList<Event>();

        events.add(new Event(
            1,
            "Mudd CS Lounge",
            16, 
            "Coffee and Cookie Social", 
            "Get free coffee and cookies while meeting fellow students", 
            12.5,
            122.34,
            0,
            "www.columbia.edu",
            1,
            "social, networking, food",
            new Timestamp(new Date().getTime() - 10), 
            new Timestamp(new Date().getTime() + 5),
            new Timestamp(new Date().getTime() + 10)
        ));

        events.add(new Event(
            2,
            "Uris Library",
            18, 
            "Midterm Study session", 
            "This is a midterm study session", 
            12.5,
            122.34,
            0,
            "www.columbia.edu",
            1,
            "study, social",
            new Timestamp(new Date().getTime() - 10), 
            new Timestamp(new Date().getTime() + 5),
            new Timestamp(new Date().getTime() + 10)
        ));

        events.add(new Event(
            3,
            "Times Square",
            21, 
            "Times Square Bowlero Bowling", 
            "Play bowling with fellow Columbia Graduate Engineers", 
            12.5,
            122.34,
            25.0f,
            "www.columbia.edu",
            1,
            "social, bowling, sports",
            new Timestamp(new Date().getTime() - 10), 
            new Timestamp(new Date().getTime() + 5),
            new Timestamp(new Date().getTime() + 10)
        ));
    }

    @Test
    void testGetNumberOfEventsByCategorySuccessfully() {
        // When
        Map<String, Integer> numberOfEventsByCategory = eventStatisticService.getNumberOfEventsByCategory(events);

        // Then
        assertEquals(6, numberOfEventsByCategory.keySet().size());
        assertTrue(numberOfEventsByCategory.containsKey("social"));
        assertTrue(numberOfEventsByCategory.containsKey("networking"));
        assertTrue(numberOfEventsByCategory.containsKey("food"));
        assertTrue(numberOfEventsByCategory.containsKey("study"));
        assertTrue(numberOfEventsByCategory.containsKey("bowling"));
        assertTrue(numberOfEventsByCategory.containsKey("sports"));
        assertEquals(3, numberOfEventsByCategory.get("social"));
        assertEquals(1, numberOfEventsByCategory.get("networking"));
        assertEquals(1, numberOfEventsByCategory.get("food"));
        assertEquals(1, numberOfEventsByCategory.get("study"));
        assertEquals(1, numberOfEventsByCategory.get("bowling"));
        assertEquals(1, numberOfEventsByCategory.get("sports"));
    }

    @Test
    void testGetNumberOfEventsByCategoryWithNullEvents() {
        // When
        Map<String, Integer> numberOfEventsByCategory = eventStatisticService.getNumberOfEventsByCategory(null);

        // Then
        assertEquals(0, numberOfEventsByCategory.keySet().size());
    }

    @Test
    void testGetNumberOfEventsByCategoryWithNoCategories() {
        // Given
        List<Event> eventsWithNoCategories = new ArrayList<Event>();
        eventsWithNoCategories.add(new Event(
            1,
            "Mudd CS Lounge",
            18, 
            "Coffee and Cookie Social", 
            "Get free coffee and cookies while meeting fellow students", 
            12.5,
            122.34,
            0,
            "www.columbia.edu",
            1,
            null,
            new Timestamp(new Date().getTime() - 10), 
            new Timestamp(new Date().getTime() + 5),
            new Timestamp(new Date().getTime() + 10)
        ));

        eventsWithNoCategories.add(new Event(
            2,
            "Uris Library",
            18, 
            "Midterm Study session", 
            "This is a midterm study session", 
            12.5,
            122.34,
            0,
            "www.columbia.edu",
            1,
            "",
            new Timestamp(new Date().getTime() - 10), 
            new Timestamp(new Date().getTime() + 5),
            new Timestamp(new Date().getTime() + 10)
        ));
        
        // When
        Map<String, Integer> numberOfEventsByCategory = eventStatisticService.getNumberOfEventsByCategory(eventsWithNoCategories);

        // Then
        assertEquals(0, numberOfEventsByCategory.keySet().size());
    }

    @Test
    void testGetAverageAgeLimitForEventsSuccessfully() {
        // When
        int averageAgeLimitForEvents = eventStatisticService.getAverageAgeLimitForEvents(events);

        // Then
        assertEquals(18, averageAgeLimitForEvents);
    }

    @Test
    void testGetAverageAgeLimitForEventsWithNullEvents() {
        // When
        int averageAgeLimitForEvents = eventStatisticService.getAverageAgeLimitForEvents(null);

        // Then
        assertEquals(0, averageAgeLimitForEvents);
    }

    @Test
    void testGetAverageAgeLimitOfEventsByCategorySuccessfully() {
        // Given
        events.add(new Event(
            4,
            "Union Square",
            50, 
            "Senior's Chess Night", 
            "Come play Chess with fellow Seniors and eat catered food truck food", 
            12.5,
            122.34,
            0,
            "www.senior-chess-nyc.com",
            1,
            "social, sports, food",
            new Timestamp(new Date().getTime() - 10), 
            new Timestamp(new Date().getTime() + 5),
            new Timestamp(new Date().getTime() + 10)
        ));

        // When
        Map<String, Integer> averageAgeLimitOfEventsByCategory = eventStatisticService.getAverageAgeLimitOfEventsByCategory(events);

        // Then
        assertEquals(6, averageAgeLimitOfEventsByCategory.keySet().size());
        assertEquals(26, averageAgeLimitOfEventsByCategory.get("social"));
        assertEquals(16, averageAgeLimitOfEventsByCategory.get("networking"));
        assertEquals(33, averageAgeLimitOfEventsByCategory.get("food"));
        assertEquals(18, averageAgeLimitOfEventsByCategory.get("study"));
        assertEquals(21, averageAgeLimitOfEventsByCategory.get("bowling"));
        assertEquals(35, averageAgeLimitOfEventsByCategory.get("sports"));
    }
}
