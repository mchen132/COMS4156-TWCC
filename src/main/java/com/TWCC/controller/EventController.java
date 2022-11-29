package com.TWCC.controller;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.hibernate.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.util.Streamable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.TWCC.data.Event;
import com.TWCC.exception.InvalidRequestException;
import com.TWCC.repository.EventRepository;
import com.TWCC.service.EventService;
import com.turkraft.springfilter.EntityFilter;

@RestController
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventService eventService;

    @GetMapping("/hello")
    public String hello() {
        return "Hi there";
    }

    @GetMapping("/events")
    public List<Event> getEvents() {
        System.out.println("getEvents() is called");
        return eventRepository.findAll();
    }

    @GetMapping("/events/{id}")
    public Optional<Event> getEventsById(@PathVariable final Integer id) {

        Optional<Event> result = eventRepository.findById(id);

        if (result == null) {
            throw new InvalidRequestException("Event ID: "
                    + id + " does not exist");
        }

        return result;
    }

    @GetMapping("/filterEvents")
    public List<Event> filterEvent(@RequestParam HashMap<String,String> allParams){
        List<Event> events = eventRepository.findAll();

        List<Event> remainingEvents = eventService.filterEvents(allParams, events);
        
        return remainingEvents;
    }

    @PostMapping("/events")
    public Event createEvent(@RequestBody final Event newEvent) {
        System.out.println("print new event");
        System.out.println("new event: " + newEvent.toString());
        return eventRepository.save(newEvent);
    }

    @PutMapping("/events")
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

    @DeleteMapping("/events/{eventId}")
    public void deleteEventById(@PathVariable(value = "eventId") Integer eventId) throws NotFoundException{
        if (eventRepository.findById(eventId).isEmpty()){
            throw new NotFoundException();
        }
        eventRepository.deleteById(eventId);
    }
}



