package com.TWCC.service;

import java.util.Arrays;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.TWCC.data.Event;
@SuppressWarnings({
    "checkstyle:AvoidInlineConditionals",
    "checkstyle:SimplifyBooleanExpression",
    "checkstyle:MethodLength",
    "checkstyle:LineLength"
})
@Service
public class EventService {
    public List<Event> filterEvents(Map<String, String> filterParams, List<Event> events) {
        if (filterParams != null && events != null) {
            List<Event> remainingEvents = new ArrayList<>(events);
            for (Map.Entry<String, String> pair : filterParams.entrySet()) {
                String key = pair.getKey();
                String value = pair.getValue();
                if (key.equals("ageLimit")) {
                    int ageLimit = Integer.parseInt(value);
                    for (
                        Iterator<Event> curEvent = remainingEvents.iterator();
                        curEvent.hasNext();
                    ) {
                        Event event = curEvent.next();
                        if (event.getAgeLimit() > ageLimit) {
                            curEvent.remove();
                        }
                    }
}
                if (key.equals("address")) {
                    for (
                        Iterator<Event> curEvent = remainingEvents.iterator();
                        curEvent.hasNext();
                    ) {
                        Event event = curEvent.next();
                        boolean findSub =
                            event.getAddress() != null
                                ? event.getAddress().toLowerCase().contains(value.toLowerCase())
                                : false;

                        if (!findSub) {
                            curEvent.remove();
                        }
                    }
                }
                if (key.equals("name")) {
                    for (
                        Iterator<Event> curEvent = remainingEvents.iterator();
                        curEvent.hasNext();
                    ) {
                        Event event = curEvent.next();
                        boolean findSub =
                            event.getName() != null
                                ? event.getName().toLowerCase().contains(value.toLowerCase())
                                : false;
                        if (!findSub) {
                            curEvent.remove();
                        }
                    }
                }
                if (key.equals("description")) {
                    for (
                        Iterator<Event> curEvent = remainingEvents.iterator();
                        curEvent.hasNext();
                    ) {
                        Event event = curEvent.next();
                        boolean findSub = event.getDescription() != null
                            ? event.getDescription().toLowerCase().contains(value.toLowerCase())
                            : false;
                        if (!findSub) {
                            curEvent.remove();
                        }
                    }
                }
                if (key.equals("cost")) {
                    int cost = Integer.parseInt(value);
                    for (
                        Iterator<Event> curEvent = remainingEvents.iterator();
                        curEvent.hasNext();
                    ) {
                        Event event = curEvent.next();
                        if (event.getCost() > cost) {
                            curEvent.remove();
                        }
                    }
                }
                if (key.equals("media")) {
                    for (
                        Iterator<Event> curEvent = remainingEvents.iterator();
                        curEvent.hasNext();
                    ) {
                        Event event = curEvent.next();
                        Boolean findSub = event.getMedia() != null
                            ? event.getMedia().toLowerCase().contains(value.toLowerCase())
                            : false;
                        if (!findSub) {
                            curEvent.remove();
                        }
                    }
                }
                if (key.equals("startTimestamp")) {
                    Timestamp startTimestamp = Timestamp.valueOf(value);
                    for (
                        Iterator<Event> curEvent = remainingEvents.iterator();
                        curEvent.hasNext();
                    ) {
                        Event event = curEvent.next();
                        int compareTime = event.getStartTimestamp() != null
                            ? event.getStartTimestamp().compareTo(startTimestamp)
                            : 0;
                        if (compareTime < 0) {
                            curEvent.remove();
                        }
                    }
                }

                if (key.equals("endTimestamp")) {
                    Timestamp endTimestamp = Timestamp.valueOf(value);
                    for (
                        Iterator<Event> curEvent = remainingEvents.iterator();
                        curEvent.hasNext();
                    ) {
                        Event event = curEvent.next();
                        int compareTime = event.getEndTimestamp() != null
                            ? event.getEndTimestamp().compareTo(endTimestamp)
                            : 0;
                        if (compareTime < 0) {
                            curEvent.remove();
                        }
                    }
                }
                if (key.equals("categories")) {
                    for (
                        Iterator<Event> curEvent = remainingEvents.iterator();
                        curEvent.hasNext();
                    ) {
                        Event event = curEvent.next();
                        String[] strSplit = event.getCategories().split("[\\s,]+");
                        ArrayList<String> caList = new ArrayList<String>(
                            Arrays.asList(strSplit)
                        );
                        System.out.println(caList);
                        for (int i = 0; i < caList.size(); i++) {
                            System.out.println(caList.get(i));
                            if (value.contains(caList.get(i)) == false && i != caList.size() - 1) {
                                continue;
                            } else if (value.contains(caList.get(i)) == false && i == caList.size() - 1) {
                                curEvent.remove();
                            }
                        }
                    }
                }
                if (key.equals("host")) {
                    int host = Integer.parseInt(value);
                    for (
                        Iterator<Event> curEvent = remainingEvents.iterator();
                        curEvent.hasNext();
                    ) {
                        Event event = curEvent.next();
                        if (event.getHost() != host) {
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
