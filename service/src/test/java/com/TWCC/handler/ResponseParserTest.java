package com.TWCC.handler;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.*;

import com.TWCC.api.ResponseParser;
import com.TWCC.data.Event;



@TestMethodOrder(MethodOrderer.Random.class)
public class ResponseParserTest {

    ResponseParser parser = new ResponseParser();
    Event event;
    static List<Event> expectedEventList;


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
                            },
                            \"classifications\": [
                                {
                                    \"primary\": true,
                                    \"segment\": {
                                        \"id\": \"KZFzniwnSyZfZ7v7nE\",
                                        \"name\": \"Sports\"
                                    },
                                    \"genre\": {
                                        \"id\": \"KnvZfZ7vAdI\",
                                        \"name\": \"Hockey\"
                                    },
                                    \"subGenre\": {
                                        \"id\": \"KZazBEonSMnZfZ7vFEE\",
                                        \"name\": \"NHL\"
                                    }
                                }
                            ]
                        }
                    ]
                }
            }
                """;
    
    @BeforeEach
    void createEvent() {
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

        String categories = "Sports,Hockey,NHL";

        event = new Event(-1, address, ageLimit, eventName, description, longitude, latitude, cost, media, -1, categories, creatioTimestamp, startTimestamp, endTimestamp);
    }

    @BeforeAll
    static void setup() {
        expectedEventList = new ArrayList<>();
    }

    @AfterEach
    void cleanup() {
        expectedEventList.clear();
    }

    @Test
    void testGetAllEventsSuccess() {
        
        List<Event> testEventList = parser.processResponse(inputJsonString);

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
                            },
                            \"classifications\": [
                                {
                                    \"primary\": true,
                                    \"segment\": {
                                        \"id\": \"KZFzniwnSyZfZ7v7nE\",
                                        \"name\": \"Sports\"
                                    },
                                    \"genre\": {
                                        \"id\": \"KnvZfZ7vAdI\",
                                        \"name\": \"Hockey\"
                                    },
                                    \"subGenre\": {
                                        \"id\": \"KZazBEonSMnZfZ7vFEE\",
                                        \"name\": \"NHL\"
                                    }
                                }
                            ]
                        }
                    ]
                }
            }
                """;

        // Event event = this.createEvent();

        List<Event> testEventList = parser.processResponse(jsonStringNoAddress);

        event.setAddress("online");
        event.setLongitude(0);
        event.setLatitude(0);

        expectedEventList.add(event);

        assertEquals(testEventList, expectedEventList);
    }

    @Test
    void testGetAllEventsNoStartDateTime() {
        String jsonStringNoStartDateTime = """
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
                                    \"localTime\": \"19:00:00\"
                                }
                            }, 
                            \"classifications\": [
                                {
                                    \"primary\": true,
                                    \"segment\": {
                                        \"id\": \"KZFzniwnSyZfZ7v7nE\",
                                        \"name\": \"Sports\"
                                    },
                                    \"genre\": {
                                        \"id\": \"KnvZfZ7vAdI\",
                                        \"name\": \"Hockey\"
                                    },
                                    \"subGenre\": {
                                        \"id\": \"KZazBEonSMnZfZ7vFEE\",
                                        \"name\": \"NHL\"
                                    }
                                }
                            ]
                        }
                    ]
                }
            }
                """;

        List<Event> testEventList = parser.processResponse(jsonStringNoStartDateTime);

        event.setCreationTimestamp(Timestamp.valueOf("2022-11-26 00:00:00"));
        event.setStartTimestamp(Timestamp.valueOf("2022-11-26 00:00:00"));
        event.setEndTimestamp(Timestamp.valueOf("2022-11-26 00:00:00"));

        expectedEventList.add(event);

        assertEquals(testEventList, expectedEventList);
    }

    @Test
    void testGetAllEventsNoPriceRangeList() {
        String jsonStringNoPriceRange = """
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
                            \"ageRestrictions\": {
                                \"legalAgeEnforced\": false
                            },
                            \"dates\": {
                                \"start\": {
                                    \"localDate\": \"2022-11-26\",
                                    \"localTime\": \"19:00:00\",
                                    \"dateTime\": \"2022-11-27T02:00:00Z\"
                                }
                            }, 
                            \"classifications\": [
                                {
                                    \"primary\": true,
                                    \"segment\": {
                                        \"id\": \"KZFzniwnSyZfZ7v7nE\",
                                        \"name\": \"Sports\"
                                    },
                                    \"genre\": {
                                        \"id\": \"KnvZfZ7vAdI\",
                                        \"name\": \"Hockey\"
                                    },
                                    \"subGenre\": {
                                        \"id\": \"KZazBEonSMnZfZ7vFEE\",
                                        \"name\": \"NHL\"
                                    }
                                }
                            ]
                        }
                    ]
                }
            }
                """;

        List<Event> testEventList = parser.processResponse(jsonStringNoPriceRange);
        
        event.setCost(0f);
        expectedEventList.add(event);

        assertEquals(testEventList, expectedEventList);

    }

    @Test
    void testGetAllEventsNoPriceRangeListContent() {
        String jsonStringNoPriceRangeListContent = """
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
                            }, 
                            \"classifications\": [
                                {
                                    \"primary\": true,
                                    \"segment\": {
                                        \"id\": \"KZFzniwnSyZfZ7v7nE\",
                                        \"name\": \"Sports\"
                                    },
                                    \"genre\": {
                                        \"id\": \"KnvZfZ7vAdI\",
                                        \"name\": \"Hockey\"
                                    },
                                    \"subGenre\": {
                                        \"id\": \"KZazBEonSMnZfZ7vFEE\",
                                        \"name\": \"NHL\"
                                    }
                                }
                            ]
                        }
                    ]
                }
            }
                """;

        List<Event> testEventList = parser.processResponse(jsonStringNoPriceRangeListContent);
        
        event.setCost(0f);
        expectedEventList.add(event);

        assertEquals(testEventList, expectedEventList);
    }

    @Test
    void testGetAllEventsNoMinCost() {
        String jsonStringNoMinCost = """
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
                            }, 
                            \"classifications\": [
                                {
                                    \"primary\": true,
                                    \"segment\": {
                                        \"id\": \"KZFzniwnSyZfZ7v7nE\",
                                        \"name\": \"Sports\"
                                    },
                                    \"genre\": {
                                        \"id\": \"KnvZfZ7vAdI\",
                                        \"name\": \"Hockey\"
                                    },
                                    \"subGenre\": {
                                        \"id\": \"KZazBEonSMnZfZ7vFEE\",
                                        \"name\": \"NHL\"
                                    }
                                }
                            ]
                        }
                    ]
                }
            }
                """;

        List<Event> testEventList = parser.processResponse(jsonStringNoMinCost);
        
        event.setCost(0f);
        expectedEventList.add(event);

        assertEquals(testEventList, expectedEventList);
    }

    @Test
    void testGetAllEventsNoAgeRestriction() {
        String jsonStringNoAgeRestriction = """
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
                            \"dates\": {
                                \"start\": {
                                    \"localDate\": \"2022-11-26\",
                                    \"localTime\": \"19:00:00\",
                                    \"dateTime\": \"2022-11-27T02:00:00Z\"
                                }
                            }, 
                            \"classifications\": [
                                {
                                    \"primary\": true,
                                    \"segment\": {
                                        \"id\": \"KZFzniwnSyZfZ7v7nE\",
                                        \"name\": \"Sports\"
                                    },
                                    \"genre\": {
                                        \"id\": \"KnvZfZ7vAdI\",
                                        \"name\": \"Hockey\"
                                    },
                                    \"subGenre\": {
                                        \"id\": \"KZazBEonSMnZfZ7vFEE\",
                                        \"name\": \"NHL\"
                                    }
                                }
                            ]
                        }
                    ]
                }
            }
                """;

        List<Event> testEventList = parser.processResponse(jsonStringNoAgeRestriction);
        
        event.setAgeLimit(0);
        expectedEventList.add(event);

        assertEquals(testEventList, expectedEventList);
    }

    @Test
    void testGetAllEventsNoLegalAgeEnforced() {
        String jsonStringNoLegalAgeEnforced = """
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
                            },
                            \"dates\": {
                                \"start\": {
                                    \"localDate\": \"2022-11-26\",
                                    \"localTime\": \"19:00:00\",
                                    \"dateTime\": \"2022-11-27T02:00:00Z\"
                                }
                            }, 
                            \"classifications\": [
                                {
                                    \"primary\": true,
                                    \"segment\": {
                                        \"id\": \"KZFzniwnSyZfZ7v7nE\",
                                        \"name\": \"Sports\"
                                    },
                                    \"genre\": {
                                        \"id\": \"KnvZfZ7vAdI\",
                                        \"name\": \"Hockey\"
                                    },
                                    \"subGenre\": {
                                        \"id\": \"KZazBEonSMnZfZ7vFEE\",
                                        \"name\": \"NHL\"
                                    }
                                }
                            ]
                        }
                    ]
                }
            }
                """;

        List<Event> testEventList = parser.processResponse(jsonStringNoLegalAgeEnforced);
        
        event.setAgeLimit(0);
        expectedEventList.add(event);

        assertEquals(testEventList, expectedEventList);
    }

    @Test
    void testGetAllEventsLegalAgeRequired() {
        String jsonStringLegalAgeRequired = """
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
                                \"legalAgeEnforced\": true
                            },
                            \"dates\": {
                                \"start\": {
                                    \"localDate\": \"2022-11-26\",
                                    \"localTime\": \"19:00:00\",
                                    \"dateTime\": \"2022-11-27T02:00:00Z\"
                                }
                            }, 
                            \"classifications\": [
                                {
                                    \"primary\": true,
                                    \"segment\": {
                                        \"id\": \"KZFzniwnSyZfZ7v7nE\",
                                        \"name\": \"Sports\"
                                    },
                                    \"genre\": {
                                        \"id\": \"KnvZfZ7vAdI\",
                                        \"name\": \"Hockey\"
                                    },
                                    \"subGenre\": {
                                        \"id\": \"KZazBEonSMnZfZ7vFEE\",
                                        \"name\": \"NHL\"
                                    }
                                }
                            ]
                        }
                    ]
                }
            }
                """;

        List<Event> testEventList = parser.processResponse(jsonStringLegalAgeRequired);
        
        event.setAgeLimit(18);
        expectedEventList.add(event);

        assertEquals(testEventList, expectedEventList);
    }

    @Test
    void testGetAllEventsNoClassifications() {
        String jsonStringLegalAgeRequired = """
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
            

        List<Event> testEventList = parser.processResponse(jsonStringLegalAgeRequired);
        
        event.setCategories(null);
        expectedEventList.add(event);

        assertEquals(testEventList, expectedEventList);
    }

    @Test
    void testGetAllEventsEmptyClassifications() {
        String jsonStringLegalAgeRequired = """
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
                            },
                            \"classifications\": [
                            ]
                        }
                    ]
                }
            }
                """;
            

        List<Event> testEventList = parser.processResponse(jsonStringLegalAgeRequired);
        
        event.setCategories(null);
        expectedEventList.add(event);

        assertEquals(testEventList, expectedEventList);
    }

    @Test
    void testGetAllEventsNoSegment() {
        String jsonStringLegalAgeRequired = """
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
                            },
                            \"classifications\": [
                                {
                                    \"primary\": true
                                }
                            ]
                        }
                    ]
                }
            }
                """;
            

        List<Event> testEventList = parser.processResponse(jsonStringLegalAgeRequired);
        
        event.setCategories(null);
        expectedEventList.add(event);

        assertEquals(testEventList, expectedEventList);
    }

    @Test
    void testGetAllEventsNoGenre() {
        String jsonStringLegalAgeRequired = """
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
                            },
                            \"classifications\": [
                                {
                                    \"primary\": true,
                                    \"segment\": {
                                        \"id\": \"KZFzniwnSyZfZ7v7nE\",
                                        \"name\": \"Sports\"
                                    }
                                }
                            ]
                        }
                    ]
                }
            }
                """;
            

        List<Event> testEventList = parser.processResponse(jsonStringLegalAgeRequired);
        
        event.setCategories("Sports");
        expectedEventList.add(event);

        assertEquals(testEventList, expectedEventList);
    }

    @Test
    void testGetAllEventsNosubGenre() {
        String jsonStringLegalAgeRequired = """
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
                            },
                            \"classifications\": [
                                {
                                    \"primary\": true,
                                    \"segment\": {
                                        \"id\": \"KZFzniwnSyZfZ7v7nE\",
                                        \"name\": \"Sports\"
                                    },
                                    \"genre\": {
                                        \"id\": \"KnvZfZ7vAdI\",
                                        \"name\": \"Hockey\"
                                    }
                                }
                            ]
                        }
                    ]
                }
            }
                """;

        List<Event> testEventList = parser.processResponse(jsonStringLegalAgeRequired);

        event.setCategories("Sports,Hockey");
        expectedEventList.add(event);

        assertEquals(testEventList, expectedEventList);
    }
}
