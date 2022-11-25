package com.TWCC.data;

import java.util.Map;

public class EventStatistics {
    private Map<String, Integer> numberOfEventsByCategory;

    public Map<String, Integer> getNumberOfEventsByCategory() {
        return numberOfEventsByCategory;
    }

    public EventStatistics setEventsByCategory(Map<String, Integer> numberOfEventsByCategory) {
        this.numberOfEventsByCategory = numberOfEventsByCategory;
        return this;
    }
}
