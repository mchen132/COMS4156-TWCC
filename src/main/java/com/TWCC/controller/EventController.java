package com.TWCC.controller;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
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
    public Event updateEvent(@RequestBody Map<String, String> jsonObject) throws NotFoundException {
        Optional<Event> optionalEvent = eventRepository.findById(Integer.parseInt(jsonObject.get("id")));
        if (optionalEvent.isEmpty()) {
            throw new NotFoundException();
        }

        Event existingEvent = optionalEvent.get();

        if (jsonObject.containsKey("address")) {
            existingEvent.setAddress(jsonObject.get("address"));
        }
        
        if (jsonObject.containsKey("ageLimit")) {
            existingEvent.setAgeLimit(Integer.parseInt(jsonObject.get("ageLimit")));
        }

        if (jsonObject.containsKey("name")) {
            existingEvent.setName(jsonObject.get("name"));
        }

        if (jsonObject.containsKey("description")) {
            existingEvent.setDescription(jsonObject.get("description"));
        }

        if (jsonObject.containsKey("longitude")) {
            existingEvent.setLongitude(Double.parseDouble(jsonObject.get("longitude")));
        }

        if (jsonObject.containsKey("latitude")) {
            existingEvent.setLatitude(Double.parseDouble(jsonObject.get("latitude")));
        }

        if (jsonObject.containsKey("cost")) {
            existingEvent.setCost(Float.parseFloat(jsonObject.get("cost")));
        }

        if (jsonObject.containsKey("media")) {
            existingEvent.setMedia(jsonObject.get("media"));
        }

        if (jsonObject.containsKey("startTimestamp")) {
            existingEvent.setStartTimestamp(Timestamp.valueOf(jsonObject.get("startTimestamp")));
        }

        if (jsonObject.containsKey("endTimestamp")) {
            existingEvent.setEndTimestamp(Timestamp.valueOf(jsonObject.get("endTimestamp")));
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



