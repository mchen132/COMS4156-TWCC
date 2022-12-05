package com.TWCC.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.TWCC.api.ApiHandler;
import com.TWCC.data.Event;

@RestController
public class AdminController {

    @Autowired
    private ApiHandler apiHandler;
    
    /**
     * Populates Event DB with events extracted from Ticketmaster API
     * 
     * @return a list of events that are extracted from Ticketmaster API
     */
    @PostMapping("/populateEvents")
    public List<Event> populateEvents() {
        
        List<Event> eventList = apiHandler.getAllEvents();
        return apiHandler.populateEvents(eventList);

    }

}
