package com.TWCC.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.TWCC.data.Event;
import com.TWCC.exception.InvalidRequestException;
import com.TWCC.repository.EventRepository;

@RestController
public class EventController {

    @Autowired
    private EventRepository eventRepository;

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

    @GetMapping("/events/byaddress/{address}")
    public List<Event> getEventsByAddress(@PathVariable final String address) {
        return eventRepository.findByAddress(address);
    }

    @GetMapping("/events/beforedate/{date}")
    public List<Event> getEventsBeforeDate(@PathVariable final String date) {
        return null;
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
    
    
    @PutMapping("/eventsPartial")
    public Event updatedEventPartial(@RequestBody Event eventRecord) throws NotFoundException {
        Optional<Event> optionalEvent = eventRepository.findById(eventRecord.getId());
        if (optionalEvent.isEmpty()) {
            throw new NotFoundException();
        }

        Event existingEvent = optionalEvent.get();

        if (eventRecord.getAddress() != null) {
            existingEvent.setAddress(eventRecord.getAddress());
        } else {
            existingEvent.setAddress(existingEvent.getAddress());
        }
        
        // numeric
        if (eventRecord.getAgeLimit() != 0) { 
            existingEvent.setAgeLimit(eventRecord.getAgeLimit());
        } else {
            existingEvent.setAgeLimit(existingEvent.getAgeLimit());
        }

        // numeric
        if (eventRecord.getCost() != 0.0) {
            existingEvent.setCost(eventRecord.getCost());
        } else {
            existingEvent.setCost(existingEvent.getCost());
        }

        if (eventRecord.getDescription() != null) {
            existingEvent.setDescription(eventRecord.getDescription());
        } else {
            existingEvent.setDescription(existingEvent.getDescription());
        }

        if (eventRecord.getEndTimestamp() != null) {
            existingEvent.setEndTimestamp(eventRecord.getEndTimestamp());
        } else {
            existingEvent.setEndTimestamp(existingEvent.getEndTimestamp());
        }

        // numeric
        if (eventRecord.getLatitude() != 0.0) {
            existingEvent.setLatitude(eventRecord.getLatitude());
        } else {
            existingEvent.setLatitude(existingEvent.getLatitude());
        }

        // numeric
        if (eventRecord.getLongitude() != 0.0) {
            existingEvent.setLongitude(eventRecord.getLongitude());
        } else {
            existingEvent.setLongitude(existingEvent.getLongitude());
        }

        if (eventRecord.getMedia() != null) {
            existingEvent.setMedia(eventRecord.getMedia());
        } else {
            existingEvent.setMedia(existingEvent.getMedia());
        }

        if (eventRecord.getName() != null) {
            existingEvent.setName(eventRecord.getName());
        } else {
            existingEvent.setName(existingEvent.getName());
        }

        if (eventRecord.getStartTimestamp() != null) {
            existingEvent.setStartTimestamp(eventRecord.getStartTimestamp());
        } else {
            existingEvent.setStartTimestamp(existingEvent.getStartTimestamp());
        }

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



