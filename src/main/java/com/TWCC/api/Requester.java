package com.TWCC.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class Requester {

    @Autowired
    private RestTemplate restTemplate;

    public String getAllEvents() {
        // String url = "https://app.ticketmaster.com/discovery/v2/events.json?apikey=GnBJz6293gftXxZJn2V82Wkre0HoJ3NR";
        // String url = "https://app.ticketmaster.com/discovery/v2/events.json?page=2&size=20&apikey=GnBJz6293gftXxZJn2V82Wkre0HoJ3NR";
        String url = "https://app.ticketmaster.com/discovery/v2/events.json?size=200&apikey=GnBJz6293gftXxZJn2V82Wkre0HoJ3NR";

        return restTemplate.getForObject(url, String.class);
    }
}