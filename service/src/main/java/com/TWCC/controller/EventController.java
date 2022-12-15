package com.TWCC.controller;

import java.sql.Timestamp;
import java.util.List;
import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import com.TWCC.data.Event;
import com.TWCC.data.EventStatistics;
import com.TWCC.payload.MessageResponse;
import com.TWCC.repository.EventRepository;
import com.TWCC.service.EventService;
import com.TWCC.security.JwtUtils;
import com.TWCC.security.UserDetailsExt;
import com.TWCC.security.UserDetailsServiceExt;
import com.TWCC.service.EventStatisticService;

@RestController
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventService eventService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceExt userDetailsService;

    @Autowired
    private EventStatisticService eventStatisticService;

    /**
     * Gets a list of all events
     *
     * @return a list of events
     */
    @GetMapping("/events")
    public List<Event> getEvents() {
        System.out.println("getEvents() is called");
        return eventRepository.findAll();
    }

    /**
     * Gets a single event matching the specified event Id
     *
     * @param id event Id
     * @return ResponseEntity containing event or failure
     */
    @GetMapping("/events/{id}")
    public ResponseEntity<?> getEventsById(@PathVariable final Integer id) {
        Optional<Event> result = eventRepository.findById(id);

        if (result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new MessageResponse(
                    "Event ID: " + id + " does not exist",
                    HttpStatus.NOT_FOUND.value()
                )
            );
        }

        return ResponseEntity.ok().body(result);
    }

    /**
     * Gets events that match a specified address
     *
     * @param address full address of the event
     * @return a list of events matching an address
     */
    @GetMapping("/events/byaddress/{address}")
    public List<Event> getEventsByAddress(@PathVariable final String address) {
        return eventRepository.findByAddress(address);
    }

    /**
     * Gets a list of filtered events from the filter query parameters.
     *
     * @param allParams Query parameters based on event fields
     * @return list of filtered events
     */
    @GetMapping("/filterEvents")
    public List<Event> filterEvent(
        @RequestParam HashMap<String, String> allParams
    ) {
        List<Event> events = eventRepository.findAll();

        List<Event> remainingEvents = eventService
            .filterEvents(allParams, events);

        return remainingEvents;
    }

    /**
     * Creates an event with the host of the event being the authorized user
     *
     * @param newEvent event object with event fields
     * @param jwt the user JWT token
     * @return ResponseEntity of the saved event or failure
     */
    @PostMapping("/events")
    public ResponseEntity<?> createEvent(
        @RequestBody final Event newEvent,
        @RequestHeader (name = "Authorization") String jwt
    ) {
        // Get user details from JWT and set host Id to new event
        String username = jwtUtils
            .getUserNameFromJwtToken(
                jwt.substring("Bearer ".length())
            );
        UserDetailsExt userDetails = (UserDetailsExt) userDetailsService
            .loadUserByUsername(username);
        newEvent.setHost(userDetails.getId());

        System.out.println("new event: " + newEvent.toString());

        try {
            Event savedEvent = eventRepository.save(newEvent);
            return ResponseEntity.ok().body(savedEvent);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                new MessageResponse(
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
                )
            );
        }
    }

    /**
     * Updates the existing event with the provided event fields
     *
     * @param jsonObject Event object
     * @return ResponseEntity of the updated event or failure
     */
    @PutMapping("/events")
    @SuppressWarnings("checkstyle:AvoidInlineConditionals")
    public ResponseEntity<?> updateEvent(
        @RequestBody HashMap<String, String> jsonObject
    ) {
        Optional<Event> optionalEvent = eventRepository
            .findById(
                Integer.parseInt(jsonObject.get("id"))
            );

        if (optionalEvent.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new MessageResponse(
                    "Event ID: " + jsonObject.get("id") + " does not exist",
                    HttpStatus.NOT_FOUND.value()
                )
            );
        }

        Event existingEvent = optionalEvent.get();

        if (jsonObject.containsKey("address")) {
            existingEvent.setAddress(jsonObject.get("address"));
        }

        if (jsonObject.containsKey("ageLimit")) {
            existingEvent.setAgeLimit(
                Integer.parseInt(jsonObject.get("ageLimit"))
            );
        }

        if (jsonObject.containsKey("name")) {
            existingEvent.setName(jsonObject.get("name"));
        }

        if (jsonObject.containsKey("description")) {
            existingEvent.setDescription(jsonObject.get("description"));
        }

        if (jsonObject.containsKey("longitude")) {
            existingEvent.setLongitude(
                Double.parseDouble(jsonObject.get("longitude"))
            );
        }

        if (jsonObject.containsKey("latitude")) {
            existingEvent.setLatitude(
                Double.parseDouble(jsonObject.get("latitude"))
            );
        }

        if (jsonObject.containsKey("cost")) {
            existingEvent.setCost(Float.parseFloat(jsonObject.get("cost")));
        }

        if (jsonObject.containsKey("media")) {
            existingEvent.setMedia(jsonObject.get("media"));
        }

        if (jsonObject.containsKey("categories")) {
            existingEvent.setCategories(jsonObject.get("categories"));
        }

        if (jsonObject.containsKey("startTimestamp")) {
            String startTimestamp = jsonObject.get("startTimestamp");
            existingEvent.setStartTimestamp(
                startTimestamp != null
                    ? Timestamp.valueOf(startTimestamp)
                    : null
                );
        }

        if (jsonObject.containsKey("endTimestamp")) {
            String endTimestamp = jsonObject.get("endTimestamp");
            existingEvent.setEndTimestamp(
                endTimestamp != null
                    ? Timestamp.valueOf(endTimestamp)
                    : null
                );
        }

        try {
            Event updatedEvent = eventRepository.save(existingEvent);
            return ResponseEntity.ok().body(
                updatedEvent != null
                    ? updatedEvent
                    : existingEvent
                );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                new MessageResponse(
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
                )
            );
        }
    }

    /**
     * Deletes the event matching the given Id
     *
     * @param eventId the event Id
     */
    @DeleteMapping("/events/{eventId}")
    public ResponseEntity<?> deleteEventById(
        @PathVariable(value = "eventId") Integer eventId
    ) {
        if (eventRepository.findById(eventId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new MessageResponse(
                    "Event ID: " + eventId + " does not exist",
                    HttpStatus.NOT_FOUND.value()
                )
            );
        }

        try {
            eventRepository.deleteById(eventId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                new MessageResponse(
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
                )
            );
        }
    }

    /**
     * Gets various statistics about TWCC events including:
     * total number of events, number of events by category,
     * average age limit for events, average age limit of events
     * by category, average cost for events, average cost of
     * events by category, and number of events by category in
     * multiple time ranges
     *
     * @return ResponseEntity containing various TWCC event statistics
     */
    @GetMapping(value = {"/events/statistics", "/events/statistics/{hostId}"})
    public ResponseEntity<?> getEventStatistics(
        @PathVariable(required = false) String hostId
    ) {
        EventStatistics eventStats = new EventStatistics();
        List<Event> events;

        if (hostId == null) {
            events = eventRepository.findAll();
        } else {
            try {
                int parsedHostId = Integer.parseInt(hostId);
                events = eventRepository.findByHost(parsedHostId);

                if (events.isEmpty()) {
                    return ResponseEntity.badRequest().body(
                        new MessageResponse(
                            String.format(
                                "There are no event statistics for this host: %s",
                                hostId
                            ),
                            HttpStatus.BAD_REQUEST.value()
                        )
                    );
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.badRequest().body(
                    new MessageResponse(
                        "hostId needs to be an integer.",
                        HttpStatus.BAD_REQUEST.value()
                    )
                );
            }
        }

        try {
            eventStats = eventStats
                .setTotalNumberOfEvents(
                    events.size()
                ).setNumberOfEventsByCategory(
                    eventStatisticService.getNumberOfEventsByCategory(events)
                ).setAverageAgeLimitForEvents(
                    eventStatisticService.getAverageAgeLimitForEvents(events)
                ).setAverageAgeLimitOfEventsByCategory(
                    eventStatisticService
                        .getAverageAgeLimitOfEventsByCategory(events)
                ).setAverageCostForEvents(
                    eventStatisticService.getAverageCostForEvents(events)
                ).setAverageCostOfEventsByCategory(
                    eventStatisticService
                        .getAverageCostOfEventsByCategory(events)
                ).setNumberOfEventsByCategoryTimeRanges(
                    eventStatisticService
                        .getNumberOfEventsByCategoryTimeRanges(events)
                );

            return ResponseEntity.ok().body(eventStats);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e);
        }
    }
}



