package com.TWCC.api;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.TWCC.data.Event;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.TextNode;

@Component
public class ResponseProcessor {
    
    // @Autowired
    // private ObjectMapper mapper;

    private ObjectMapper mapper = new ObjectMapper();

    public List<Event> processResponse(String jsonString) {

        Map<String, Object> responseMap = this.jsonStringToMap(jsonString);

        @SuppressWarnings("unchecked")
        Map<String, Object> eventMap = (HashMap<String, Object>) responseMap.get("_embedded");
        
        @SuppressWarnings("unchecked")
        ArrayList<Object> eventList = (ArrayList<Object>) eventMap.get("events");

        return objectListToEventList(eventList);

    }

    private List<Event> objectListToEventList(List<Object> objList) {
        
        return objList.stream().map(this::objectToEvent).collect(Collectors.toList());

    }

    private Event objectToEvent(Object obj) {

        @SuppressWarnings("unchecked")
        Map<String, Object> eventMap = (HashMap<String, Object>) obj;

        int id = -1;
        String address = this.extractAddress(eventMap);

        int ageLimit = this.extractAgeLimit(eventMap);

        String name = String.valueOf(eventMap.get("name"));

        // remove surrounding double-quotes
        name = name.substring(1, name.length() - 1); 

        // ticketmater event API does not have description field
        String description = name;

        List<Double> locationPair = this.extractCoordinates(eventMap);

        double longitude = locationPair.get(0).doubleValue();
        double latitude = locationPair.get(1).doubleValue();

        float cost = this.extractCost(eventMap);

        String media = String.valueOf(eventMap.get("url"));

        // Remove surrounding double-quotes
        media = media.substring(1, media.length() - 1);

        // Ticketmaster API does not provide event end time and creation time
        Timestamp startTimestamp = this.extractStartTimestamp(eventMap);
        Timestamp creationTimestamp = startTimestamp;
        Timestamp endTimestamp = startTimestamp;

        return new Event(id, address, ageLimit, name, description, longitude, latitude, cost, media, creationTimestamp, startTimestamp, endTimestamp);
    }

    // All location related info are embedded in events[_embedded][venues]
    private Map<String, Object> extractVenueMap(Map<String, Object> eventMap) {

        @SuppressWarnings("unchecked")
        Map<String, Object> venueMap = (HashMap<String, Object>)
                                        ((ArrayList<Object>)
                                        ((HashMap<String, Object>) 
                                        eventMap
                                        .get("_embedded"))
                                        .get("venues"))
                                        .get(0);
        
        return venueMap;
    }

    private List<Double> extractCoordinates(Map<String, Object> eventMap) {

        List<Double> res = new ArrayList<>();

        Map<String, Object> venueMap = this.extractVenueMap(eventMap);

        @SuppressWarnings("unchecked")
        Map<String, Object> locationMap = (HashMap<String, Object>)venueMap.get("location");


        String longitudeString = ((TextNode)locationMap.get("longitude")).toString();

        // remove surrounding quotes
        longitudeString = longitudeString.substring(1, longitudeString.length() - 1);

        String latitudeString = ((TextNode)locationMap.get("latitude")).toString();

        // remove surrounding quotes
        latitudeString = latitudeString.substring(1, latitudeString.length() - 1);

        res.add(Double.valueOf(longitudeString));
        res.add(Double.valueOf(latitudeString));

        return res;
        
    }

    private String extractAddress(Map<String, Object> eventMap) {

        Map<String, Object> venueMap = this.extractVenueMap(eventMap);

        // venue JSON sometimes containes only a {href: String} pair
        // which directs to another API call to get venue details. 
        // venue JSON sometimes also contain the detailed information 
        // about the venue. 
        // Such pattern is unpredictable

        if (venueMap.size() == 1) {
            return "online";
        }

        // venue name
        String name = String.valueOf(venueMap.get("name"));

        // removing double quotes from the original string value
        name = name.substring(1, name.length() - 1); 

        @SuppressWarnings("unchecked")
        Map<String, Object> streetAddrMap = (HashMap<String, Object>)venueMap.get("address");

        String streetAddr = "";

        for (String key : streetAddrMap.keySet()) {

            String addr = String.valueOf(streetAddrMap.get(key));

            // removing double quotes from the original string value
            addr = " " + addr.substring(1, addr.length() - 1);

            streetAddr += addr;
        }

        @SuppressWarnings("unchecked")
        String city = String.valueOf(((HashMap<String, Object>)venueMap.get("city")).get("name"));

        // removing double quotes from the original string value
        city = city.substring(1, city.length() - 1);

        @SuppressWarnings("unchecked")
        String state = String.valueOf(((HashMap<String, Object>)venueMap.get("state")).get("stateCode"));

        // removing double quotes from the original string value
        state = state.substring(1, state.length() - 1);

        @SuppressWarnings("unchecked")
        String country = String.valueOf(((HashMap<String, Object>)venueMap.get("country")).get("countryCode"));

        // removing double quotes from the original string value
        country = country.substring(1, country.length() - 1);

        return String.format("%s,%s, %s, %s, %s", name, streetAddr, city, state, country);
    }

    private Timestamp extractStartTimestamp (Map<String, Object> eventMap) {


        @SuppressWarnings("unchecked")
        Map<String, Object> startTimeMap = (HashMap<String, Object>)((HashMap<String, Object>)eventMap.get("dates")).get("start");

        // start time's DateTime is missing from the response body
        if (!startTimeMap.containsKey("dateTime")) {
            String localDateString = String.valueOf(startTimeMap.get("localDate"));

            localDateString = localDateString.substring(1, localDateString.length() - 1);

            localDateString += " 00:00:00";

            System.out.println(localDateString);

            return Timestamp.valueOf(localDateString);
        }

        String startTimestampString = String.valueOf(startTimeMap.get("dateTime"));

        startTimestampString = startTimestampString.substring(1, startTimestampString.length() - 1);

        Instant dateTime = Instant.parse(startTimestampString);

        return Timestamp.from(dateTime);
    }

    private float extractCost (Map<String, Object> eventMap) {

        float res = 0;

        if (eventMap.get("priceRanges") == null){
            return res;
        }

        @SuppressWarnings("unchecked")
        ArrayList<Object> rangeList = (ArrayList<Object>)eventMap.get("priceRanges");

        if (rangeList.size() == 0) {
            return res;
        }
        
        @SuppressWarnings("unchecked")
        Map<String, Object> priceMap = (HashMap<String, Object>) rangeList.get(0);

        if (priceMap.get("min") == null) {
            return res;
        }

        return (float) ((DoubleNode)priceMap.get("min")).asDouble();
    }

    private int extractAgeLimit (Map<String, Object> eventMap) {

        int ageLimit = 0;

        if (!eventMap.containsKey("ageRestrictions")) {
            return ageLimit;
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> restrictionMap = (HashMap<String, Object>) eventMap.get("ageRestrictions");

        if (!restrictionMap.containsKey("legalAgeEnforced")) {
            return ageLimit;
        }

        boolean ageRestriction = ((BooleanNode)restrictionMap.get("legalAgeEnforced")).booleanValue();

        ageLimit = ageRestriction ? 18 : 0;

        return ageLimit;
    }

    // NOTE: stringToMap(), toMap(), and toList() are inspired by the following
    // stackoverflow post: https://stackoverflow.com/a/24012023

    // convert a JSON string into a Map
    private Map<String, Object> jsonStringToMap(String jsonString) {
        Map<String, Object> retMap = new HashMap<>();

        try {
            JsonNode node = this.mapper.readTree(jsonString);

            if (node != null) {
                retMap = this.toMap(node);
            }

            return retMap;

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return retMap;
    }

    // Convert a JsonNode tree to a Json in the form of a Map
    // where map's value are essentially JsonNode objects;
    // we can use JsonNode's own methods to check and get values 
    private Map<String, Object> toMap(JsonNode rootNode) {
        
        Map<String, Object> retMap = new HashMap<>();

        Iterator<Entry<String, JsonNode>> iter = rootNode.fields();

        iter.forEachRemaining(field -> {
            String key = field.getKey();
            JsonNode currNode = field.getValue();
            Object value = currNode;

            if (currNode.isArray()) {
                value = this.toList(currNode);
            }else if (currNode.isObject()){
                value = this.toMap(currNode);
            }

            retMap.put(key, value);

        });

        return retMap;

    }

    // convert an Array JsonNode to a list of JsonNodes
    private List<Object> toList(JsonNode rootNode) {
        List<Object> retList = new ArrayList<>();

        Iterator<JsonNode> iter = rootNode.elements();

        iter.forEachRemaining(currNode -> {
            Object value = currNode;

            if (currNode.isObject()) {
                value = this.toMap(currNode);
            } else if (currNode.isArray()) {
                value = this.toList(currNode);
            }

            retList.add(value);
        });

        return retList;

    }

};