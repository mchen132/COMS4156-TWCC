package com.TWCC.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.TWCC.data.Event;

@Service
public class EventService {

    /**
     * Gets back a list of filtered events based on the filter params and events passed in.
     * 
     * @param filterParams Map<String, String> of Event fields as keys and their values to filter on
     * @param events List of events
     * @return Filtered list of events
     */
    public List<Event> filterEvents(Map<String, String> filterParams, List<Event> events) {
        if (filterParams != null && events != null) {
            List<Event> remainingEvents = new ArrayList<>(events);
            for (Map.Entry<String, String> pair : filterParams.entrySet()) {
                String key = pair.getKey();
                String value = pair.getValue();
    
                if (key.equals("ageLimit")) {
                    int age_limit = Integer.parseInt(value);
                    for (Iterator<Event> curEvent = remainingEvents.iterator(); curEvent.hasNext();) {
                        Event event = curEvent.next();
                                            
                        if (event.getAgeLimit() > age_limit) {
                            curEvent.remove();
                        }
                    }
                }
                
                if (key.equals("address")) {
                    for (Iterator<Event> curEvent = remainingEvents.iterator(); curEvent.hasNext();) {
                        Event event = curEvent.next();
                        boolean findSub = event.getAddress().toLowerCase().contains(value.toLowerCase());
                        if (findSub == false) {
                            curEvent.remove();
                        }
                    }
                }
    
                if (key.equals("name")) {              
                    for (Iterator<Event> curEvent = remainingEvents.iterator(); curEvent.hasNext();) {
                        Event event = curEvent.next();
                        boolean findSub = event.getName().toLowerCase().contains(value.toLowerCase());
                        if (findSub == false) {
                            curEvent.remove();
                        }
    
                    }
                }
    
                if (key.equals("description")) {
                    for (Iterator<Event> curEvent = remainingEvents.iterator(); curEvent.hasNext();) {
                        Event event = curEvent.next();
                        boolean findSub = event.getDescription().toLowerCase().contains(value.toLowerCase());
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
                        Boolean findSub = event.getMedia().toLowerCase().contains(value.toLowerCase());
                        if (findSub == false) {
                            curEvent.remove();
                        }
                    }
                }
    
                if (key.equals("startTimestamp")) {   
                    Timestamp start_timestamp = Timestamp.valueOf(value);
                    for (Iterator<Event> curEvent = remainingEvents.iterator(); curEvent.hasNext();) {
                        Event event = curEvent.next();
                        int compareTime = event.getStartTimestamp().compareTo(start_timestamp);
                        if (compareTime < 0) {
                            curEvent.remove();
                        }
                    }
                }
    
                if (key.equals("endTimestamp")) {   
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
        } else {
            return new ArrayList<Event>();
        }
    }
}
