package com.TWCC.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Component
public class ApiHandler {
    
    @Autowired
    private Requester requester;
    
    @Autowired
    private ResponseProcessor processor;


    public Map<String, Object> getResponse() {

        String responseString = requester.getPlainJSON();

        try {
            return processor.processResponse(responseString);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return new HashMap<String, Object>();

    }

    public String getStringResponse() {
        return requester.getPlainJSON();
    }

}