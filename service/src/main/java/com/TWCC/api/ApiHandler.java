package com.TWCC.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.TWCC.data.Event;

@Component
public class ApiHandler {
    
    @Autowired
    private RequestService requester;
    
    @Autowired
    private ResponseParser parser;


    public List<Event> getAllEvents() {

        String responseString = requester.getAllEvents();
        return parser.processResponse(responseString);

    }

}