package com.TWCC.data;

import java.util.Map;

public class EventStatistics {
    private Map<String, Integer> numberOfEventsByCategory;
    private int averageAgeLimitForEvents;
    private float averageCostForEvents;
    private Map<String, Float> averageCostOfEventsByCategory;
    private Map<String, Integer> averageAgeLimitOfEventsByCategory;
    private Map<String, Map<String, Integer>> numberOfEventsByCategoryTimeRanges;

    public Map<String, Integer> getNumberOfEventsByCategory() {
        return numberOfEventsByCategory;
    }

    public EventStatistics setEventsByCategory(Map<String, Integer> numberOfEventsByCategory) {
        this.numberOfEventsByCategory = numberOfEventsByCategory;
        return this;
    }

    public int getAverageAgeLimitForEvents() {
        return averageAgeLimitForEvents;
    }

    public EventStatistics setAverageAgeLimitForEvents(int averageAgeLimitForEvents) {
        this.averageAgeLimitForEvents = averageAgeLimitForEvents;
        return this;
    }

    public float getAverageCostForEvents() {
        return averageCostForEvents;
    }

    public EventStatistics setAverageCostForEvents(float averageCostForEvents) {
        this.averageCostForEvents = averageCostForEvents;
        return this;
    }

    public Map<String, Float> getAverageCostOfEventsByCategory() {
        return averageCostOfEventsByCategory;
    }

    public EventStatistics setAverageCostOfEventsByCategory(Map<String, Float> averageCostOfEventsByCategory) {
        this.averageCostOfEventsByCategory = averageCostOfEventsByCategory;
        return this;
    }

    public Map<String, Integer> getAverageAgeLimitOfEventsByCategory() {
        return averageAgeLimitOfEventsByCategory;
    }

    public EventStatistics setAverageAgeLimitOfEventsByCategory(Map<String, Integer> averageAgeLimitOfEventsByCategory) {
        this.averageAgeLimitOfEventsByCategory = averageAgeLimitOfEventsByCategory;
        return this;
    }

    public Map<String, Map<String, Integer>> getNumberOfEventsByCategoryTimeRanges() {
        return numberOfEventsByCategoryTimeRanges;
    }

    public EventStatistics setNumberOfEventsByCategoryTimeRanges(Map<String, Map<String, Integer>> numberOfEventsByCategoryTimeRanges) {
        this.numberOfEventsByCategoryTimeRanges = numberOfEventsByCategoryTimeRanges;
        return this;
    }
}
