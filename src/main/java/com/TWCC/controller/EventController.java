package com.TWCC.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.TWCC.data.Event;
import com.TWCC.data.EventRepository;
import com.TWCC.exception.InvalidRequestException;

@RestController
public class EventController {

    @Autowired
    EventRepository eventRepository;

    @GetMapping("/hello")
    public String Hello() {
        return "Hi there!";
    }

    @GetMapping("/events")
    public List<Event> getEvents() {
        System.out.println("getEvents() is calls");
        return eventRepository.findAll();
    }

    @GetMapping("/events/{id}")
    public Optional<Event> getEventsById(@PathVariable Integer id) {
        
        Optional<Event> result = eventRepository.findById(id);
        
        if (result == null) {
            throw new InvalidRequestException("Event ID: " + id + " does not exist");
        }

        return result;
    }

    @GetMapping("/events/byaddress/{address}")
    public List<Event> getEventsByAddress(@PathVariable String address) {
        return eventRepository.findByAddress(address);
    }

    @GetMapping("/events/beforedate/{date}")
    public List<Event> getEventsBeforeDate(@PathVariable String date) {
        // TODO: return events before a certain date
        return null;
    }
}