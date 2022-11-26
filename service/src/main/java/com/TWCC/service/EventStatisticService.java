package com.TWCC.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.TWCC.data.Event;

public class EventStatisticService {
    /**
     * Gets number of events per event category
     * 
     * @param events list of events
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

    /**
     * Gets the average age limit out of all of the existing events
     * 
     * @param events list of events
     * @return the average age limit for events
     */
    public int getAverageAgeLimitForEvents(List<Event> events) {
        int totalNumberOfEvents = events.size();
        int ageLimitTotal = 0;
        for (Event event: events) {
            ageLimitTotal += event.getAgeLimit();
        }

        return ageLimitTotal / totalNumberOfEvents;
    }

    /**
     * Gets the average cost for all of the existing events
     * 
     * @param events list of events
     * @return the average cost for events
     */
    public float getAverageCostForEvents(List<Event> events) {
        int totalNumberOfEvents = events.size();
        float costTotal = 0;
        for (Event event: events) {
            costTotal += event.getCost();
        }

        return costTotal / totalNumberOfEvents;
    }

    /**
     * Gets the average cost of events per event category
     * 
     * @param events list of events
     * @return the average cost of events per event category
     */
    public Map<String, Float> getAverageCostOfEventsByCategory(List<Event> events) {
        Map<String, Float> averageCostOfEventsByCategory = new HashMap<String, Float>();
        Map<String, Integer> numberOfEventsByCategory = new HashMap<String, Integer>();

        for (Event event: events) {
            String[] categories = event.getCategories().split(",");

            for (int i = 0; i < categories.length; i++) {
                String category = categories[i];

                if (averageCostOfEventsByCategory.containsKey(category)) {
                    averageCostOfEventsByCategory.put(category, averageCostOfEventsByCategory.get(category) + event.getCost());
                    numberOfEventsByCategory.put(category, numberOfEventsByCategory.get(category) + 1);
                } else {
                    averageCostOfEventsByCategory.put(category, event.getCost());
                    numberOfEventsByCategory.put(category, 1);
                }
            }
        }

        // Calculate average
        for (String category: averageCostOfEventsByCategory.keySet()) {
            Float averageCost = averageCostOfEventsByCategory.get(category) / numberOfEventsByCategory.get(category);
            averageCostOfEventsByCategory.put(category, averageCost);
        }

        return averageCostOfEventsByCategory;
    }

    /**
     * Gets the average age limit of events per event category
     * 
     * @param events list of events
     * @return the average age limit of events per event category
     */
    public Map<String, Integer> getAverageAgeLimitOfEventsByCategory(List<Event> events) {
        Map<String, Integer> averageAgeLimitOfEventsByCategory = new HashMap<String, Integer>();
        Map<String, Integer> numberOfEventsByCategory = new HashMap<String, Integer>();

        for (Event event: events) {
            String[] categories = event.getCategories().split(",");

            for (int i = 0; i < categories.length; i++) {
                String category = categories[i];

                if (averageAgeLimitOfEventsByCategory.containsKey(category)) {
                    averageAgeLimitOfEventsByCategory.put(category, averageAgeLimitOfEventsByCategory.get(category) + event.getAgeLimit());
                    numberOfEventsByCategory.put(category, numberOfEventsByCategory.get(category) + 1);
                } else {
                    averageAgeLimitOfEventsByCategory.put(category, event.getAgeLimit());
                    numberOfEventsByCategory.put(category, 1);
                }
            }
        }

        // Calculate average
        for (String category: averageAgeLimitOfEventsByCategory.keySet()) {
            int averageAgeLimit = averageAgeLimitOfEventsByCategory.get(category) / numberOfEventsByCategory.get(category);
            averageAgeLimitOfEventsByCategory.put(category, averageAgeLimit);
        }

        return averageAgeLimitOfEventsByCategory;
    }
}
