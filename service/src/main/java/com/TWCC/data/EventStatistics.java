package com.TWCC.data;

import java.util.Map;

public class EventStatistics {
    private Map<String, Integer> eventsByCategory;

    public Map<String, Integer> getEventsByCategory() {
        return eventsByCategory;
    }

    public EventStatistics setEventsByCategory(Map<String, Integer> eventsByCategory) {
        this.eventsByCategory = eventsByCategory;
        return this;
    }
}
