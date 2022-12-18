package com.TWCC.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.TWCC.data.Event;
import com.TWCC.repository.EventRepository;

@TestMethodOrder(MethodOrderer.Random.class)
@SpringBootTest
@ActiveProfiles("test")
public class AdminControllerTestIntegrationTest {

    @Autowired
    AdminController adminController;

    @Autowired
    EventRepository eventRepository;

    private static final int API_RESPONSE_MIN_SIZE = 100;

    @Test
    void testPopulateEvents() {
        // When

        List<Event> responseEventList = adminController.populateEvents();
        List<Event> savedEventList = eventRepository.findAll();

        // Then
        assertTrue(responseEventList.size() > API_RESPONSE_MIN_SIZE);
        assertTrue(savedEventList.size() > API_RESPONSE_MIN_SIZE);
        assertTrue(CollectionUtils.isEqualCollection(responseEventList, savedEventList));
    }
}
