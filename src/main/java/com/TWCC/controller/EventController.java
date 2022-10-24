package com.TWCC.controller;

import java.util.List;

import org.apache.catalina.startup.ClassLoaderFactory.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.TWCC.data.Event;
import com.TWCC.data.EventRepository;
import com.mysql.cj.protocol.Message;

@RestController
public class EventController {

    @Autowired
    EventRepository eventRepository;

    @GetMapping("/hello")
    public String hello() {
        return "Hello!";
    }

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
}