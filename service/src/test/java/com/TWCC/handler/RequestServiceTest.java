package com.TWCC.handler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.MethodOrderer;

import com.TWCC.api.RequestService;


@TestMethodOrder(MethodOrderer.Random.class)
@ExtendWith(MockitoExtension.class)
public class RequestServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private RequestService service;


    @Test
    void testGetAllEvents() {
        String response = "OK!";
        String url = "https://app.ticketmaster.com/discovery/v2/events.json?size=200&apikey=GnBJz6293gftXxZJn2V82Wkre0HoJ3NR";

        Mockito
        .when(restTemplate.getForObject(url, String.class))
        .thenReturn("OK!");

        String testResponse = service.getAllEvents();
        assertEquals(response, testResponse);
    }
}
