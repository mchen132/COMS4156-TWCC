package com.TWCC.service;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.TWCC.data.Event;


@TestMethodOrder(MethodOrderer.Random.class)
@SpringBootTest
@ActiveProfiles("test")
public class EventStatisticServiceTest {
    private static EventStatisticService eventStatisticService;
    private List<Event> events;
    private List<Event> eventsWithNoCategories;
    
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
            new Timestamp(new GregorianCalendar(2022, 11, 1).getTimeInMillis()),
            new Timestamp(new GregorianCalendar(2022, 11, 2).getTimeInMillis())
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
            new Timestamp(new GregorianCalendar(2022, 11, 10).getTimeInMillis()),
            new Timestamp(new GregorianCalendar(2022, 11, 11).getTimeInMillis())
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
            new Timestamp(new GregorianCalendar(2022, 11, 20).getTimeInMillis()),
            new Timestamp(new GregorianCalendar(2022, 11, 21).getTimeInMillis())
        ));

        eventsWithNoCategories = new ArrayList<Event>();

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

    @Test
    void testGetAverageAgeLimitOfEventsByCategoryWithNullEvents() {        
        // When
        Map<String, Integer> averageAgeLimitOfEventsByCategory = eventStatisticService.getAverageAgeLimitOfEventsByCategory(null);
        
        // Then
        assertEquals(0, averageAgeLimitOfEventsByCategory.keySet().size());
    }

    @Test
    void testGetAverageAgeLimitOfEventsByCategoryWithNoCategories() {
        // When
        Map<String, Integer> averageAgeLimitOfEventsByCategory = eventStatisticService.getAverageAgeLimitOfEventsByCategory(eventsWithNoCategories);

        // Then
        assertEquals(0, averageAgeLimitOfEventsByCategory.keySet().size());
    }

    @Test
    void testGetAverageCostForEventsSuccessfully() {
        // Given
        events.add(new Event(
            4,
            "Union Square",
            10, 
            "Table Tennis Competition", 
            "Come compete in a local NYC table tennis competition", 
            12.5,
            122.34,
            10,
            "www.union-square-competitions.com",
            1,
            "social, sports",
            new Timestamp(new Date().getTime() - 10), 
            new Timestamp(new Date().getTime() + 5),
            new Timestamp(new Date().getTime() + 10)
        ));

        // When
        float averageCostForEvents = eventStatisticService.getAverageCostForEvents(events);

        // Then
        assertEquals(8.75, averageCostForEvents);
    }

    @Test
    void testGetAverageCostForEventsWithNullEvents() {
        // When
        float averageCostForEvents = eventStatisticService.getAverageCostForEvents(null);

        // Then
        assertEquals(0, averageCostForEvents);
    }

    @Test
    void testGetAverageCostOfEventsByCategorySuccessfully() {
        // Given
        events.add(new Event(
            4,
            "Union Square",
            10, 
            "Table Tennis Competition", 
            "Come compete in a local NYC table tennis competition", 
            12.5,
            122.34,
            10,
            "www.union-square-competitions.com",
            1,
            "social, sports",
            new Timestamp(new Date().getTime() - 10), 
            new Timestamp(new Date().getTime() + 5),
            new Timestamp(new Date().getTime() + 10)
        ));
        
        // When
        Map<String, Float> averageCostOfEventsByCategory = eventStatisticService.getAverageCostOfEventsByCategory(events);

        // Then
        assertEquals(6, averageCostOfEventsByCategory.keySet().size());
        assertEquals(8.75f, averageCostOfEventsByCategory.get("social"));
        assertEquals(0, averageCostOfEventsByCategory.get("networking"));
        assertEquals(0, averageCostOfEventsByCategory.get("food"));
        assertEquals(0, averageCostOfEventsByCategory.get("study"));
        assertEquals(25, averageCostOfEventsByCategory.get("bowling"));
        assertEquals(17.5f, averageCostOfEventsByCategory.get("sports"));
    }

    @Test
    void testGetAverageCostOfEventsByCategoryWithNullEvents() {
        // When
        Map<String, Float> averageCostOfEventsByCategory = eventStatisticService.getAverageCostOfEventsByCategory(null);

        // Then
        assertEquals(0, averageCostOfEventsByCategory.keySet().size());
    }

    @Test
    void testGetAverageCostOfEventsByCategoryWithNoCategories() {
        // When
        Map<String, Float> averageCostOfEventsByCategory = eventStatisticService.getAverageCostOfEventsByCategory(eventsWithNoCategories);

        // Then
        assertEquals(0, averageCostOfEventsByCategory.keySet().size());
    }

    @Test
    void testGetNumberOfEventsByCategoryTimeRangesSuccessfully() {
        // Given
        events.add(new Event(
            4,
            "Union Square",
            10, 
            "Table Tennis Competition", 
            "Come compete in a local NYC table tennis competition", 
            12.5,
            122.34,
            10,
            "www.union-square-competitions.com",
            1,
            "social, sports",
            new Timestamp(new Date().getTime() - 10), 
            new Timestamp(new GregorianCalendar(2022, 11, 19).getTimeInMillis()),
            new Timestamp(new GregorianCalendar(2022, 11, 20).getTimeInMillis())
        ));
        
        // When
        Map<String, Map<String, Integer>> numberOfEventsByCategoryTimeRanges = eventStatisticService.getNumberOfEventsByCategoryTimeRanges(events);

        // Then
        assertEquals(3, numberOfEventsByCategoryTimeRanges.keySet().size());
        assert (numberOfEventsByCategoryTimeRanges.containsKey("11-2022-1"));
        assert (numberOfEventsByCategoryTimeRanges.containsKey("11-2022-2"));
        assert (numberOfEventsByCategoryTimeRanges.containsKey("11-2022-4"));
        Map<String, Integer> numberOfEventsByCategory1 = numberOfEventsByCategoryTimeRanges.get("11-2022-1");
        Map<String, Integer> numberOfEventsByCategory2 = numberOfEventsByCategoryTimeRanges.get("11-2022-2");
        Map<String, Integer> numberOfEventsByCategory3 = numberOfEventsByCategoryTimeRanges.get("11-2022-4");
        assertEquals(1, numberOfEventsByCategory1.get("social"));
        assertEquals(1, numberOfEventsByCategory1.get("networking"));
        assertEquals(1, numberOfEventsByCategory1.get("food"));
        assertEquals(1, numberOfEventsByCategory2.get("study"));
        assertEquals(1, numberOfEventsByCategory2.get("social"));
        assertEquals(2, numberOfEventsByCategory3.get("sports"));
        assertEquals(2, numberOfEventsByCategory3.get("social"));
        assertEquals(1, numberOfEventsByCategory3.get("bowling"));
    }

    @Test
    void testGetNumberOfEventsByCategoryTimeRangesWithNullEvents() {
        // When
        Map<String, Map<String, Integer>> numberOfEventsByCategoryTimeRanges = eventStatisticService.getNumberOfEventsByCategoryTimeRanges(null);

        // Then
        assertEquals(0, numberOfEventsByCategoryTimeRanges.keySet().size());
    }

    @Test
    void testGetNumberOfEventsByCategoryTimeRangesWithNoCategories() {
        // When
        Map<String, Map<String, Integer>> numberOfEventsByCategoryTimeRanges = eventStatisticService.getNumberOfEventsByCategoryTimeRanges(eventsWithNoCategories);

        // Then
        assertEquals(0, numberOfEventsByCategoryTimeRanges.keySet().size());
    }
}
