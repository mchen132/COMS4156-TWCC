package com.TWCC.controller;

import java.util.List;
import java.util.Optional;

import javax.naming.NameNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException.NotFound;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.data.repository.CrudRepository;

import com.TWCC.data.Event;
import com.TWCC.repository.EventRepository;

@RestController
public class EventController {

    @Autowired
    EventRepository eventRepository;

    @GetMapping("/hello")
    public String hello() {
        return "Hi there";
    }

    @GetMapping("/event")
    public List<Event> getEvents() {
        System.out.println("getEvents() is calls");
        return eventRepository.findAll();
    }

    @GetMapping("/event/{eventId}")
    public Event getEventById(@PathVariable int eventId) {
        System.out.println("getEventById is called");
        return eventRepository.findById(eventId).get();
    }

    // @PutMapping("/eventUpdate")
    // public Event updateEventName(@RequestBody Integer eventId, String newEventName) throws NotFoundException {
    //     if (eventId == null || newEventName == null) {
    //         throw new RuntimeException("eventId and newEventName cannot be null.");
    //     }
    //     Optional<Event> optionalEvent = eventRepository.findById(eventId);
    //     if (optionalEvent.isEmpty()) {
    //         throw new NotFoundException();
    //     }
    //     System.out.println("updateEventName is called...");

    //     Event existingEvent = optionalEvent.get();
    //     existingEvent.setName(newEventName);
    //     return existingEvent;
    // }

    @PutMapping("eventUpdate")
    public Event updateEvent(@RequestBody Event eventRecord) throws NotFoundException {
        Optional<Event> optionalEvent = eventRepository.findById(eventRecord.getId());
        if (optionalEvent.isEmpty()) {
            throw new NotFoundException();
        }

        Event existingEvent = optionalEvent.get();

        existingEvent.setAddress(eventRecord.getAddress());
        existingEvent.setAgeLimit(eventRecord.getAgeLimit());
        existingEvent.setCost(eventRecord.getCost());
        existingEvent.setDescription(eventRecord.getDescription());
        existingEvent.setEndTimestamp(eventRecord.getEndTimestamp());
        existingEvent.setLatitude(eventRecord.getLatitude());
        existingEvent.setLongitude(eventRecord.getLongitude());
        existingEvent.setMedia(eventRecord.getMedia());
        existingEvent.setName(eventRecord.getName());
        existingEvent.setStartTimestamp(eventRecord.getStartTimestamp());

        return eventRepository.save(existingEvent);
    }

    @PostMapping("/events")
    public Event createEvent(@RequestBody Event newEvent) {
        System.out.println("Create new event: " + newEvent.toString());
        return eventRepository.save(newEvent);
    }
}