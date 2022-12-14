package com.TWCC.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.TWCC.api.ApiHandler;
import com.TWCC.data.Event;
import com.TWCC.repository.EventRepository;

@TestMethodOrder(MethodOrderer.Random.class)
@ExtendWith(MockitoExtension.class)
public class ApiHandlerTest {
    
    @Mock
    EventRepository eventRepository;

    @InjectMocks
    private ApiHandler apiHandler;

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

    @AfterEach
    void tearDown() {
        events.clear();
    }

    @Test
    void testPopulateEvent() {
        
        Mockito.when(eventRepository.save(any(Event.class))).thenAnswer(i -> i.getArguments()[0]);

        List<Event> response = apiHandler.populateEvents(events);

        verify(eventRepository, times(3)).save(any(Event.class));

        for (int idx = 0; idx < events.size(); idx ++) {

            assertEquals(events.get(idx), response.get(idx));

        }

    }
}
