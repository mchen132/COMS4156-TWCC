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
import com.turkraft.springfilter.EntityFilter;

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

    // @GetMapping("/events/byaddress/{address}")
    // public List<Event> getEventsByAddress(@PathVariable final String address) {
    //     return eventRepository.findByAddress(address);
    // }

    



    @GetMapping("/filterEvents")
    public List<Event> filterEvent(@RequestParam HashMap<String,String> allParams){
        Map<String, String> test = allParams;

        List<Event> remainingEvents = eventRepository.findAll();

        for (Map.Entry<String, String> pair : test.entrySet()) {
            String key = pair.getKey();
            String value = pair.getValue();
            System.out.println(key);
            System.out.println(value);

            if (key.equals("age_limit")) {
                System.out.println("hiiii");
                int age_limit = Integer.parseInt(value);
                for (Iterator<Event> curEvent = remainingEvents.iterator(); curEvent.hasNext();) {
                    Event event = curEvent.next();
                    // System.out.println(event);
                    if (event.getAgeLimit() > age_limit) {
                        curEvent.remove();
                    }
                }
                }
            
            if (key.equals("address")) {
                for (Iterator<Event> curEvent = remainingEvents.iterator(); curEvent.hasNext();) {
                    Event event = curEvent.next();
                    boolean findSub = event.getAddress().contains(value.toLowerCase());
                    if (findSub == false) {
                        curEvent.remove();
                    }
                }
            }

            if (key.equals("name")) {              
                for (Iterator<Event> curEvent = remainingEvents.iterator(); curEvent.hasNext();) {
                    Event event = curEvent.next();
                    boolean findSub = event.getName().contains(value.toLowerCase());
                    if (findSub == false) {
                        curEvent.remove();
                    }

                }
            }

            if (key.equals("description")) {
                for (Iterator<Event> curEvent = remainingEvents.iterator(); curEvent.hasNext();) {
                    Event event = curEvent.next();
                    boolean findSub = event.getDescription().contains(value.toLowerCase());
                    if (findSub == false) {
                        curEvent.remove();
                    }
                }
            }

            if (key.equals("cost")) {
                int cost = Integer.parseInt(value);
                for (Iterator<Event> curEvent = remainingEvents.iterator(); curEvent.hasNext();) {
                    Event event = curEvent.next();
                    if (event.getCost() > cost) {
                        curEvent.remove();
                    }
                }
                }

            if (key.equals("media")) {
                for (Iterator<Event> curEvent = remainingEvents.iterator(); curEvent.hasNext();) {
                    Event event = curEvent.next();
                    Boolean findSub = event.getMedia().contains(value.toLowerCase());
                    if (findSub == false) {
                        curEvent.remove();
                    }
                }
            }

            if (key.equals("start_timestamp")) {   
                Timestamp start_timestamp = Timestamp.valueOf(value);
                for (Iterator<Event> curEvent = remainingEvents.iterator(); curEvent.hasNext();) {
                    Event event = curEvent.next();
                    int compareTime = event.getStartTimestamp().compareTo(start_timestamp);
                    if (compareTime < 0) {
                        curEvent.remove();
                    }
                }
            }

            if (key.equals("end_timestamp")) {   
                Timestamp end_timestamp = Timestamp.valueOf(value);
                for (Iterator<Event> curEvent = remainingEvents.iterator(); curEvent.hasNext();) {
                    Event event = curEvent.next();
                    int compareTime = event.getEndTimestamp().compareTo(end_timestamp);
                    if (compareTime < 0) {
                        curEvent.remove();
                    }
                }
            }

            
            }
        
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



