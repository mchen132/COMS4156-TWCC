package com.TWCC.service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.TWCC.data.Event;

@Component
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
     * Gets the average age limit of events per event category
     * 
     * @param events list of events
     * @return the average age limit of events per event category
     */
    public Map<String, Integer> getAverageAgeLimitOfEventsByCategory(List<Event> events) {
        Map<String, Integer> averageAgeLimitOfEventsByCategory = new HashMap<String, Integer>();
        Map<String, Integer> numberOfEventsByCategory = new HashMap<String, Integer>();

        for (Event event: events) {
            if (event.getCategories() != null) {
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
        }

        // Calculate average
        for (String category: averageAgeLimitOfEventsByCategory.keySet()) {
            int averageAgeLimit = averageAgeLimitOfEventsByCategory.get(category) / numberOfEventsByCategory.get(category);
            averageAgeLimitOfEventsByCategory.put(category, averageAgeLimit);
        }

        return averageAgeLimitOfEventsByCategory;
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
            if (event.getCategories() != null) {
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
        }

        // Calculate average
        for (String category: averageCostOfEventsByCategory.keySet()) {
            Float averageCost = averageCostOfEventsByCategory.get(category) / numberOfEventsByCategory.get(category);
            averageCostOfEventsByCategory.put(category, averageCost);
        }

        return averageCostOfEventsByCategory;
    }

    /**
     * Gets the number of events for a category in a given time range (month-year-weekInMonth)
     * for all existing events 
     * 
     * @param events list of events
     * @return the number of events for a category in a given time range
     */
    public Map<String, Map<String, Integer>> getNumberOfEventsByCategoryTimeRanges(List<Event> events) {
        Map<String, Map<String, Integer>> numberOfEventsByCategoryTimeRanges = new HashMap<String, Map<String, Integer>>();
        Calendar cal = Calendar.getInstance();
        for (Event event: events) {
            if (event.getStartTimestamp() != null && event.getCategories() != null) {
                Timestamp startTimestamp = event.getStartTimestamp();
                String[] categories = event.getCategories().split(",");
    
                cal.setTimeInMillis(startTimestamp.getTime());
                int eventMonth = cal.get(Calendar.MONTH);
                int eventYear = cal.get(Calendar.YEAR);
                int eventWeekOfMonth = cal.get(Calendar.WEEK_OF_MONTH);
                System.out.println("eventMonth: " + eventMonth);
                System.out.println("eventYear: " + eventYear);
                System.out.println("eventWeekOfMonth" + eventWeekOfMonth);
                
                String monthYearWeek = String.format("%d-%d-%d", eventMonth, eventYear, eventWeekOfMonth);
                for (int i = 0; i < categories.length; i++) {
                    String category = categories[i];

                    if (numberOfEventsByCategoryTimeRanges.containsKey(monthYearWeek)) {
                        Map<String, Integer> numOfEventsByCategory = numberOfEventsByCategoryTimeRanges.get(monthYearWeek);
                            
                        if (numOfEventsByCategory.containsKey(category)) {
                            numOfEventsByCategory.put(category, numOfEventsByCategory.get(category) + 1);
                            numberOfEventsByCategoryTimeRanges.put(
                                monthYearWeek,
                                numOfEventsByCategory
                            );
                        } else {
                            numOfEventsByCategory.put(category, 1);
                            numberOfEventsByCategoryTimeRanges.put(
                                monthYearWeek,
                                numOfEventsByCategory
                            );
                        }                        
                    } else {
                        Map<String, Integer> numOfEventsByCategory = new HashMap<String, Integer>();
                        numOfEventsByCategory.put(category, 1);
                        numberOfEventsByCategoryTimeRanges.put(
                            monthYearWeek,
                            numOfEventsByCategory
                        );
                    }
                }
            }
        }

        return numberOfEventsByCategoryTimeRanges;
    }
}
