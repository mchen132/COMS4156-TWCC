package com.TWCC.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.TWCC.data.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    
}
