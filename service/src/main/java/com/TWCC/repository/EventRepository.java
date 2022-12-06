package com.TWCC.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.TWCC.data.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> findByAddress(String address);
}



