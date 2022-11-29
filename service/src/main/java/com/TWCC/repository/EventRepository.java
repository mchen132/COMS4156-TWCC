package com.TWCC.repository;


import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.TWCC.data.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    // List<Event> findByAddressOrAgeLimitOrNameOrDescriptionOrLongitudeOrLatitudeOrCostOrMediaOrStartTimeStampOrEndTimeStamp(String addr, int age_limit, String name, String description, Double longitude, Double latitude, Float cost, String media, Timestamp start_timestamp, Timestamp end_timestamp);
    List<Event> findByAddress(String address);
  


    
    // List<Event> findAll(Specification<Event> spec, Pageable page);

}



