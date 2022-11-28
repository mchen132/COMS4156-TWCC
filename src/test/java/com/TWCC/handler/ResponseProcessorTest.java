package com.TWCC.handler;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.TWCC.api.ResponseProcessor;
import com.TWCC.data.Event;

// @ExtendWith(MockitoExtension.class)
// @ExtendWith(SpringExtension.class)
// @SpringBootTest
// @ContextConfiguration(classes = { SpringBootConfiguration.class })
// @JsonTest
// @ContextConfiguration
// @AutoConfigureJson
public class ResponseProcessorTest {

    // @Autowired
    // ResponseProcessor processor;

    ResponseProcessor processor = new ResponseProcessor();

    private String inputJsonString = """
            {
                \"_embedded\": {
                    \"events\": [
                        {
                            \"name\": \"Phoenix Suns vs. Utah Jazz\", 
                            \"url\": \"https://www.ticketmaster.com/phoenix-suns-vs-utah-jazz-phoenix-arizona-11-26-2022/event/19005D0B8FA91548\",
                            \"_embedded\": {
                                \"venues\": [
                                    {
                                        \"name\": \"Footprint Center\",
                                        \"city\": {
                                            \"name\": \"Phoenix\"
                                        }, 
                                        \"state\": {
                                            \"name\": \"Arizona\", 
                                            \"stateCode\": \"AZ\"
                                        },
                                        \"country\": {
                                            \"name\": \"United States of America\", 
                                            \"countryCode\": \"US\"
                                        }, 
                                        \"address\": {
                                            \"line1\": \"201 East Jefferson Street\"
                                        }, 
                                        \"location\": {
                                            \"longitude\": \"-112.071313\", 
                                            \"latitude\": \"33.445899\"
                                        }
                                    }
                                ]
                            }, 
                            \"priceRanges\": [
                                {
                                    \"type\": \"standard\", 
                                    \"currency\": \"USD\", 
                                    \"min\": 46.0, 
                                    \"max\": 574.0
                                }
                            ], 
                            \"ageRestrictions\": {
                                \"legalAgeEnforced\": false
                            },
                            \"dates\": {
                                \"start\": {
                                    \"localDate\": \"2022-11-26\",
                                    \"localTime\": \"19:00:00\",
                                    \"dateTime\": \"2022-11-27T02:00:00Z\"
                                }
                            }
                        }
                    ]
                }
            }
                """;
    
    private Event createEvent() {
        String eventName = "Phoenix Suns vs. Utah Jazz";
        String description = eventName;

        float cost = (float) 46.0;


        String media = "https://www.ticketmaster.com/phoenix-suns-vs-utah-jazz-phoenix-arizona-11-26-2022/event/19005D0B8FA91548";

        int ageLimit = 0;

        String startTimestampString = "2022-11-27T02:00:00Z";

        Instant dateTime = Instant.parse(startTimestampString);

        Timestamp startTimestamp = Timestamp.from(dateTime);
        Timestamp endTimestamp = startTimestamp;
        Timestamp creatioTimestamp = startTimestamp;

        String address = "Footprint Center, 201 East Jefferson Street, Phoenix, AZ, US";

        double longitude = -112.071313;
        double latitude = 33.445899;

        return new Event(-1, address, ageLimit, eventName, description, longitude, latitude, cost, media, creatioTimestamp, startTimestamp, endTimestamp);
    }

    // private List<Event> createExpectedList () {
    //     List<Event> eventList = new ArrayList<>();

    //     String eventName = "Phoenix Suns vs. Utah Jazz";
    //     String description = eventName;

    //     float cost = (float) 46.0;


    //     String media = "https://www.ticketmaster.com/phoenix-suns-vs-utah-jazz-phoenix-arizona-11-26-2022/event/19005D0B8FA91548";

    //     int ageLimit = 0;

    //     String startTimestampString = "2022-11-27T02:00:00Z";

    //     Instant dateTime = Instant.parse(startTimestampString);

    //     Timestamp startTimestamp = Timestamp.from(dateTime);
    //     Timestamp endTimestamp = startTimestamp;
    //     Timestamp creatioTimestamp = startTimestamp;

    //     String address = "Footprint Center, 201 East Jefferson Street, Phoenix, AZ, US";

    //     double longitude = -112.071313;
    //     double latitude = 33.445899;

    //     Event event = new Event(-1, address, ageLimit, eventName, description, longitude, latitude, cost, media, creatioTimestamp, startTimestamp, endTimestamp);

    //     eventList.add(event);

    //     return eventList;
    // }

    @Test
    void testGetAllEventsSuccess() {
        
        List<Event> testEventList = processor.processResponse(inputJsonString);
        // List<Event> expectedEventList = this.createExpectedList();
        Event event = this.createEvent();
        List<Event> expectedEventList = new ArrayList<>();

        expectedEventList.add(event);

        assertEquals(testEventList, expectedEventList);
        
    }

    @Test
    void testGetAllEventsNoAddress() {
        String jsonStringNoAddress = """
            {
                \"_embedded\": {
                    \"events\": [
                        {
                            \"name\": \"Phoenix Suns vs. Utah Jazz\", 
                            \"url\": \"https://www.ticketmaster.com/phoenix-suns-vs-utah-jazz-phoenix-arizona-11-26-2022/event/19005D0B8FA91548\",
                            \"_embedded\": {
                                \"venues\": [
                                    {
                                        \"href\": \"/discovery/v2/venues/KovZpZAE617A?locale=en-us\"
                                    }
                                ]
                            }, 
                            \"priceRanges\": [
                                {
                                    \"type\": \"standard\", 
                                    \"currency\": \"USD\", 
                                    \"min\": 46.0, 
                                    \"max\": 574.0
                                }
                            ], 
                            \"ageRestrictions\": {
                                \"legalAgeEnforced\": false
                            },
                            \"dates\": {
                                \"start\": {
                                    \"localDate\": \"2022-11-26\",
                                    \"localTime\": \"19:00:00\",
                                    \"dateTime\": \"2022-11-27T02:00:00Z\"
                                }
                            }
                        }
                    ]
                }
            }
                """;
        
        Event event = this.createEvent();

        List<Event> testEventList = processor.processResponse(jsonStringNoAddress);
        List<Event> expectedEventList = new ArrayList<>();

        event.setAddress("online");
        event.setLongitude(0);
        event.setLatitude(0);

        expectedEventList.add(event);

        assertEquals(testEventList, expectedEventList);
    }
}
