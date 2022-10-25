package com.TWCC.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.TWCC.data.Event;
import com.TWCC.exception.InvalidRequestException;
import com.TWCC.repository.EventRepository;

@RestController
public class EventController {

    @Autowired
    private EventRepository eventRepository;

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

    @DeleteMapping("/delete/{eventId}")
    public void deleteEventById(@PathVariable(value = "eventId") Integer eventId) throws NotFoundException{
        if (eventRepository.findById(eventId).isEmpty()){
            throw new NotFoundException();
        }
        eventRepository.deleteById(eventId);
    }

    @PostMapping("/events")
    public Event createEvent(@RequestBody final Event newEvent) {
        System.out.println("print new event");
        System.out.println("new event: " + newEvent.toString());
        return eventRepository.save(newEvent);
    }
}