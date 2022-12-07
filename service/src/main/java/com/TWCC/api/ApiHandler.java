package com.TWCC.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.TWCC.data.Event;
import com.TWCC.repository.EventRepository;

@Service
public class ApiHandler {
    @Autowired
    private RequestService requester;

    @Autowired
    private ResponseParser parser;

    @Autowired
    private EventRepository eventRepository;


    /**
     * Get first 200 events of all events from TicketMaster
     *
     * @return a list of events
     */
    public List<Event> getAllEvents() {

        String responseString = requester.getAllEvents();
        return parser.processResponse(responseString);

    }

    /**
     * Stores a list of events to the Event DB
     *
     * @param List<Event>
     * @return a list of events that are actually stored
     */
    public List<Event> populateEvents(List<Event> events) {

        List<Event> response = new ArrayList<>();

        for (Event event: events) {

            try {
                response.add(eventRepository.save(event));
            } catch (Exception e) {
                System.out.println("Skip event");
                System.out.println(event + "\n");
            }

        }

        return response;

    }

}
