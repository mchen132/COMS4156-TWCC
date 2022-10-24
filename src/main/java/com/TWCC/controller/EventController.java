package com.TWCC.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.TWCC.data.Event;
import com.TWCC.repository.EventRepository;

@RestController
public class EventController {

    @Autowired
    EventRepository eventRepository;

    @GetMapping("/event")
    public List<Event> getEvents() {
        System.out.println("getEvents() is calls");
        return eventRepository.findAll();
    }

    @DeleteMapping("/delete/{eventId}")
    public void deleteEventById(@PathVariable(value = "eventId") Integer eventId) throws NotFoundException{
        if (eventRepository.findById(eventId).isEmpty()){
            throw new NotFoundException();
        }
        eventRepository.deleteById(eventId);
    }

    @PostMapping("/events")
    public Event createEvent(@RequestBody Event newEvent) {
        System.out.println("Create new event: " + newEvent.toString());
        return eventRepository.save(newEvent);
    }
}