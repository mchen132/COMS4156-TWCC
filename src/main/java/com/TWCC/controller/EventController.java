package com.TWCC.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.TWCC.data.Event;
import com.TWCC.data.EventRepository;

@RestController
public class EventController {

    @Autowired
    EventRepository eventRepository;

    @GetMapping("/events")
    public List<Event> getEvents() {
        System.out.println("getEvents() is calls");
        return eventRepository.findAll();
    }
}