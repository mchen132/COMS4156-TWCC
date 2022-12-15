package com.TWCC.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

import java.sql.Timestamp;
import java.time.Instant;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;


import com.TWCC.api.ApiHandler;
import com.TWCC.api.RequestService;
import com.TWCC.data.Event;
import com.TWCC.repository.EventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings({"checkstyle:AvoidInlineConditionals", "checkstyle:TodoCommentCheck",
"checkstyle:LineLengthCheck",
"checkstyle:StaticVariableNameCheck", "checkstyle:MagicNumberCheck",
"checkstyle:VisibilityModifierCheck", "checkstyle:FileTabCharacterCheck", "checkstyle:SimplifyBooleanExpressionCheck", "checkstyle:MethodLengthCheck", "checkstyle:RegexpSinglelineCheck"})

@WebMvcTest(AdminController.class)
@ExtendWith(MockitoExtension.class)
public class AdminControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    EventRepository eventRepository;

    @MockBean
    private ApiHandler apiHandler;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private RequestService service;

    private static Event event;

    private static String CSRF_TOKEN_NAME;
	private static HttpSessionCsrfTokenRepository csrfTokenRepo;

    private static String apiUrl;
    private static String responseString;

    @BeforeAll
    static void beforeClass() {
        // CSRF Token Setup
		CSRF_TOKEN_NAME = "org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN";
		csrfTokenRepo = new HttpSessionCsrfTokenRepository();

        // Ticketmater URL setup
        apiUrl = "https://app.ticketmaster.com/discovery/v2/events.json?size=200&apikey=GnBJz6293gftXxZJn2V82Wkre0HoJ3NR";

        // Event object setup
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

        event = new Event(-1, address, ageLimit, eventName, description, longitude, latitude, cost, media, -1, "testCategory", creatioTimestamp, startTimestamp, endTimestamp);

        // Ticketmaster API response setup
        responseString = """
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
    }


    @Test
	@WithMockUser
    void testPopulateEvents() {
        
        Mockito.when(eventRepository.save(any(Event.class))).thenAnswer(i -> i.getArguments()[0]);

        Mockito
        .when(restTemplate.getForObject(apiUrl, String.class))
        .thenReturn(responseString);

        try {

			CsrfToken csrfToken = (CsrfToken) csrfTokenRepo.generateToken(new MockHttpServletRequest());

            MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/populateEvents")
                .sessionAttr(CSRF_TOKEN_NAME, csrfToken) // Need to pass CSRF Token for POST requests
                .param(csrfToken.getParameterName(), csrfToken.getToken())
                .header("Authorization", csrfToken.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
            
    		mockMvc.perform(mockRequest)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.[0][id]", is(-1)))
				.andExpect(jsonPath("$.address", is("Footprint Center, 201 East Jefferson Street, Phoenix, AZ, US")))
				.andExpect(jsonPath("$.ageLimit", is(0)))
				.andExpect(jsonPath("$.name", is("Phoenix Suns vs. Utah Jazz")))
				.andExpect(jsonPath("$.description", is("Phoenix Suns vs. Utah Jazz")))
				.andExpect(jsonPath("$.longitude", is(-112.071313)))
				.andExpect(jsonPath("$.latitude", is(33.445899)))
				.andExpect(jsonPath("$.cost", is(46.0)))
				.andExpect(jsonPath("$.media", is("https://www.ticketmaster.com/phoenix-suns-vs-utah-jazz-phoenix-arizona-11-26-2022/event/19005D0B8FA91548")))
				.andExpect(jsonPath("$.categories", is("testCategory")));
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
}
