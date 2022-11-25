package com.TWCC.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.TWCC.data.Event;

public class EventStatisticService {
    /**
     * Gets number of events per event category
     * 
     * @param events
     * @return Map<String, Integer> of number of events per category of events
     */
    public Map<String, Integer> getNumberOfEventsByCategory(List<Event> events) {
        Map<String, Integer> numberOfEventsByCategory = new HashMap<String, Integer>();
        for (Event event: events) {
            if (event.getCategories() != null) {
                String[] categories = event.getCategories().split(",");
    
                for (int i = 0; i < categories.length; i++) {
                    String category = categories[i];
    
                    if (numberOfEventsByCategory.containsKey(category)) {
                        numberOfEventsByCategory.put(category, numberOfEventsByCategory.get(category) + 1);
                    } else {
                        numberOfEventsByCategory.put(category, 1);
                    }
                }
            }
        }

        return numberOfEventsByCategory;
    }
}
