package com.TWCC.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class Requester {

    @Autowired
    private RestTemplate restTemplate;

    public String getPlainJSON() {
        String url = "https://app.ticketmaster.com/discovery/v2/events.json?apikey=GnBJz6293gftXxZJn2V82Wkre0HoJ3NR&city=[easton]";

        return restTemplate.getForObject(url, String.class);
    }
}