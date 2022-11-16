package com.TWCC.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ResponseProcessor {
    
    @Autowired
    private ObjectMapper mapper;

    public Map<String, Object> processResponse(String response) throws JsonMappingException, JsonProcessingException {

        return this.mapper.readValue(response, new TypeReference<Map<String, Object>>() {});
    }

};