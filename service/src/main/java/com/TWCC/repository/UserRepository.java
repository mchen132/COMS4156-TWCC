package com.TWCC.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.TWCC.data.TwccUser;

@Repository
public interface UserRepository extends JpaRepository<TwccUser, Integer> {
    Optional<TwccUser> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
