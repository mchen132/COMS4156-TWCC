package com.TWCC.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.TWCC.data.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    
}
